/*********************************************************************
Matto Project
2016-11-02

시리얼 통신 프로토콜
JSON 형태로
	1. 문열기 (성별은 F, M)
		{
			"cmd" : "dopen",
			"data" : "F"
		}

		return
		{
			"status" : "S/F",
		}

	2.센싱값 전송
		 {
		 	"cmd" : "sensing"
		}
			return
		{
			"status" : "S/F",
			"tem" : "온도",
			"hum" : "습도",
			"tissue" : "휴지부족여부",
			"count" : "화장실 인원",
			"room" : "사용중인칸수"
		}


핀번호 매칭
A0 남적외선밖
A1 남적외선안
A2 여적외선밖
A3 여적외선안
2 DHT
3 휴지센서
4 휴지 LED
5 남도어락
6 여도어락
7 여칸1
8 여칸2
9 칸LED


*********************************************************************/

// ----------------------------------선언부----------------------------------

#include "/Library/Timer-master/Timer.h"	//Timer 라이브러리
#include "/Library/ArduinoJson/ArduinoJson.h" //JSON 라이브러리

//핀 셋팅
#define DHTPIN 2     //센서 핀번호
int pin_tissueSens=3;		//휴지센서
int pin_tissueLed=4;		//휴지 LED
char pin_doorOutMan=A0;		//남입구밖
char pin_doorInMan=A1;	//남입구안
char pin_doorOutWom=A2;  //여입구밖
char pin_doorInWom=A3;   //여입구안
int pin_doorlockMan=5;		//남도어락
int pin_doorlockWom=6;    //여도어락
int pin_room1=7;    //1번칸
int pin_room2=8;    //2번칸
int pin_roomLed=9;  //칸LED

//온습도센서
#include "DHT.h"	//온습도센서 라이브러리 (DHT22)
#define DHTTYPE DHT22   // DHT 22  (AM2302), AM2321

DHT dht(DHTPIN, DHTTYPE);
float humidity;
float temperature;

//서보모터 (도어락 기능)
#include <Servo.h>
Servo womDoorlock,manDoorlock;
int lock = 170;
int unlock = 95;

//시간을 위한 변수
unsigned long current_time=0;
unsigned long prev_time=0;
Timer t;
int notPassWom=0;
int notPassMan=0;
bool in=false,out=false,inMoment=false,outMoment=false;

//조건들
int doorInterval = 850;		//입구센서 카운트 딜레이
//0A41SK 4~30cm 센서 센싱값 = 2076/(analogRead(A0)-11)
//2A0A02 20~150cm 센서 센싱값 = 10650.08 * pow(valInWom,-0.935) - 10;
// 9462/(analogRead(A1)-16.92)
float cond_womEntDistance = 8.0;
float cond_manEntDistance = 50.0;




//센서값
int val_doorInWom;
int val_doorOutWom;
int val_doorInMan;
int val_doorOutMan;
int val_tissue;
int val_roomWom[2];
int number_personWom=0;
int save_personWom=0;
int number_personMan=0;
int save_personMan=0;

//통신에 필요한 변수
char in_data[300] = "";
bool needCommu=false;
bool endCommu=false;

// ----------------------------------선언부끝----------------------------------

// ----------------------------------메인부----------------------------------
void setup(){
	pinMode(pin_tissueLed,OUTPUT);
	pinMode(pin_roomLed,OUTPUT);
	Serial.begin(9600);
	dht.begin();	//온습도센서 로딩
	//도어락 연결
	womDoorlock.attach(pin_doorlockWom);
	manDoorlock.attach(pin_doorlockMan);
	womDoorlock.write(lock);
	manDoorlock.write(lock);
	sensing_tissue();
	t.every(1000,sensing_tissue);	//1분마다 휴지체크
}

void loop(){
	t.update();
	current_time = millis();
	sensing_door(pin_doorInWom,pin_doorOutWom,pin_doorInMan,pin_doorOutMan);	//입구센서 센싱
	doorCount();	//센싱값으로 인원처리
	if(Serial.available()){
		fromSerial();	//시리얼통신으로 받을게 있으면 커뮤니케이션 함수 실행
	}
}

// ----------------------------------메인부끝---------------------------------


// ----------------------------------통신부----------------------------------
void fromSerial(){	//수신 코드
	static int i=0;
	char data = Serial.read();	//일단 읽음
	if(needCommu){			//읽는 중이었다면
		if(data == '}'){	//마지막을 알리는 경우 설정갑 초기화와 함께 파싱 진행
			in_data[i++]=data;
			in_data[i]='\0';
			i=0;
			needCommu = false;
			endCommu = true;
		}
		else{
			in_data[i++] = data;	//내용의 중간인 경우 in_data에 계속 추가
		}
	}
	else{
		if(data == '{'){
			needCommu = true;	//내용의 시작을 알리면 읽기 모드 돌입
			in_data[i++]=data;
		}
	}
	if(endCommu){
		//시리얼 값 다 받으면 JSON파싱
		StaticJsonBuffer<200> jsonBufferIn;
		JsonObject& recv = jsonBufferIn.parseObject(in_data);
		//JSON분석을 통한 실행
		parseData(recv);
		endCommu=false;
	}
}

void parseData(JsonObject& parse){
	StaticJsonBuffer<200> jsonBufferOut;	//버퍼설정
	JsonObject& send = jsonBufferOut.createObject();
	char* cmd = parse["cmd"];	//명령어 파싱
	//명령어가 dopen이면
	if(strcmp(cmd,"dopen")==0){
		char* data = parse["data"];
		if(strcmp(data,"F") || strcmp(data,"M")){
			if(doorOpen(data)==0){
				send["status"] = "S";
			} else {
				send["status"] = "F";
			}
		} else {
			send["status"] = "F";
		}
	}
	//명령어가 sensing이면
	else if(strcmp(cmd,"sensing")==0){
		//JSON오브젝트에 각 값을 추가
		send["status"] = "S";
		sensing_dht();
		// if (isnan(humidity) || isnan(temperature)){
			//
		// }
		// else {
			send["tem"] = temperature;
			send["hum"] = humidity;
		// }
		send["tissue"] = val_tissue;
		send["count"] = number_personWom;
		send["room"] = sensing_room();
	}
	else{
		send["status"] = "F";
	}
	//값 시리얼로 전송
	send.printTo(Serial); Serial.println("");
}
// ----------------------------------통신부끝----------------------------------


// ----------------------------------센서부----------------------------------

int doorOpen(char* toilet){
	if (strcmp(toilet,"F")==0){
		womDoorlock.write(unlock);
		save_personWom = number_personWom;
		notPassWom = t.every(1000,doorClose);
	}	else if(strcmp(toilet,"M")==0){
		manDoorlock.write(unlock);
		save_personMan = number_personMan;
		notPassMan = t.every(1000,doorClose);
	}
	else {
		return 1;
	}
	return 0;
}
void doorClose(){
	// Serial.println("Not Closed");
	if((notPassWom!=0) && (number_personWom>save_personWom)){
		womDoorlock.write(lock);
		t.stop(notPassWom);
		notPassWom=0;
		save_personWom = 0;
	} else if((notPassMan!=0) && (number_personMan>save_personMan)){
		manDoorlock.write(lock);
		t.stop(notPassMan);
		notPassMan=0;
		save_personMan = 0;
	}
}

void sensing_tissue(){
	val_tissue = digitalRead(pin_tissueSens);
	//val_tissue를 반전시킨 값으로 LED를 온/오프 (휴지가 부족한 경우 val_tissue값이 0)
	digitalWrite(pin_tissueLed,val_tissue==1?0:1);
}

void sensing_dht(){
	humidity = dht.readHumidity();  //습도
	temperature = dht.readTemperature();  //온도 (C)
}

int sensing_room(){
	digitalWrite(pin_roomLed,1);
	delay(50);
	val_roomWom[0] = digitalRead(pin_room1);
	val_roomWom[1] = digitalRead(pin_room2);
	int sum=0;
	for(int cnt=0; cnt<2; cnt++){
		if(val_roomWom[cnt] == 1){
			sum++;
		}
	}
	digitalWrite(pin_roomLed,0);
	return sum;
}

//입구센서 센싱
void sensing_door(char inPinWom, char outPinWom, char inPinMan, char outPinMan){
	int valInWom = analogRead(inPinWom);
	int valOutWom = analogRead(outPinWom);
	int valInMan = analogRead(inPinMan);
	int valOutMan = analogRead(outPinMan);
	float cmInWom = 2076/(valInWom-11);	//그래프를 수식화한 식
	float cmOutWom = 2076/(valOutWom-11);
	float cmInMan = 10650.08 * pow(valInMan,-0.935) - 10;
	float cmOutMan = 10650.08 * pow(valInMan,-0.935) - 10;
	// Serial.print(cmOutWom); Serial.print("  "); Serial.println(cmInWom);
	//여자 안
	if(cmInWom<cond_womEntDistance){
			val_doorInWom = 1;
	}	else{
		val_doorInWom = 0;
	}
	//여자 밖
	if(cmOutWom<cond_womEntDistance){
		val_doorOutWom = 1;
	}	else{
		val_doorOutWom = 0;
	}
	//남자 안
	if(cmInMan<cond_manEntDistance){
		val_doorInMan = 1;
	}	else{
		val_doorInMan = 0;
	}
	//남자 밖
	if(cmOutMan<cond_manEntDistance){
			val_doorOutMan = 1;
	}	else{
		val_doorOutMan = 0;
	}
}

//입구센서 카운팅 함수 (센싱 되어있어야 함)
void doorCount(){
  // Serial.print(val_doorInWom); Serial.print("  "); Serial.println(val_doorOutWom);
	//안쪽센서 인식시
	if(val_doorInWom == 1 && val_doorOutWom == 0){
		if(out == false && !inMoment){
			// Serial.println("intrue");
			// delay(1000);
			in = true;
		} else if (out){
			number_personWom++;
			Serial.println(number_personWom);
			// delay(1000);
			inMoment = true;
			in=out=false;
			t.after(doorInterval,unMomentIn);
		}
	}
	//바깥쪽센서 인식시
	if(val_doorOutWom == 1 && val_doorInWom == 0){
		if(in == false && !outMoment){
			// Serial.println("outture");
			// delay(1000);
			out = true;
		} else if (in){
			if(number_personWom>0){
				number_personWom--;
				Serial.println(number_personWom);
				// delay(1000);
			}
			in=out=false;
			outMoment = true;
			t.after(doorInterval,unMomentOut);
		}
	}
}

void unMomentIn(){
	inMoment = false;
}
void unMomentOut(){
	outMoment = false;
}
// ----------------------------------센서부끝----------------------------------
