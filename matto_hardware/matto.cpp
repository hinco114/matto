/*********************************************************************
Matto Project
2016-10-03

시리얼 통신 프로토콜
JSON 형태로
	1. "cmd" : "DOPEN"
	문열기
	2. "cmd" : "SENSING"
	센싱값 전송


핀번호 매칭 다시 해야함.

*********************************************************************/

// ----------------------------------선언부----------------------------------

#include <Timer.h>	//Timer 라이브러리
#include <ArduinoJson.h> //JSON 라이브러리

//온습도센서
#include "DHT.h"	//온습도센서 라이브러리 (DHT22)
#define DHTTYPE DHT22   // DHT 22  (AM2302), AM2321
#define DHTPIN 2     //센서 핀번호
DHT dht(DHTPIN, DHTTYPE);
float humidity;
float temperature;

//서보모터 (도어락 기능)
#include <Servo.h>
Servo doorlock;

//시간을 위한 변수
unsigned long current_time=0;
unsigned long prev_time=0;
Timer t;
int notPass=0;

//조건들
int interval_temp = 2000;	//온습도센서 카운트 딜레이
int interval = 700;		//입구센서 카운트 딜레이
//0A41SK 4~30cm 센서 센싱값 = 2076/(analogRead(A0)-11)
//2A0A02 20~150cm 센서 센싱값 = 10650.08 * pow(Sensor_inner,-0.935) - 10;
// 9462/(analogRead(A1)-16.92)
float distance_enter = 10.0;

//핀 셋팅
int tissue_pin=4;		//휴지센서
int tissue_led=5;		//휴지 LED
char door_outter_pin=A0;		//입구 안쪽센서
char door_inner_pin=A1;	//입구 바깥쪽센서
int doorlock_pin=3;		//도어락(서보모터)


//센서값
float door_sens[2];
int tissue_sens;
int sector_sens[2];
int number_person=0;
int save_person=0;

//통신에 필요한 변수
char in_data[300] = "";
bool needCommu=false;
bool endCommu=false;

// ----------------------------------선언부끝----------------------------------

// ----------------------------------메인부----------------------------------
void setup(){
	pinMode(tissue_led,OUTPUT);	
	Serial.begin(9600);
	dht.begin();	//온습도센서 로딩
	doorlock.attach(doorlock_pin);	//도어락 연결
	t.every(60*1000,sensing_tissue);	//1분마다 휴지체크
}

void loop(){
	t.update();
	current_time = millis();
	sensing_door(door_inner_pin,door_outter_pin);	//입구센서 센싱
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
	//명령어가 DOPEN이면
	if(strcmp(cmd,"DOPEN")==0){
		doorOpen();
		send["status"] = "S";
	}
	//명령어가 SENSING이면
	else if(strcmp(cmd,"SENSING")==0){
		//JSON오브젝트에 각 값을 추가
		send["status"] = "S";
		sensing_dht();
		if (isnan(humidity) || isnan(temperature)){}
		else {
			send["tem"] = temperature;
			send["hum"] = humidity;
		}
		send["tissue"] = tissue_sens;
		send["count"] = number_person;	
	}
	else{
		send["status"] = "F";
	}
	//값 시리얼로 전송
	send.printTo(Serial); Serial.println("");
}
// ----------------------------------통신부끝----------------------------------


// ----------------------------------센서부----------------------------------

// Delay 없이 타이머를 이용하고, 일정시간 지나면 문이 잠기도록 해야함
void doorOpen(){
	Serial.println("Door OPEND!");
	doorlock.write(21);
	save_person = number_person;
	notPass = t.every(1000,doorClose);
}
void doorClose(){
	Serial.println("Not Closed");
	if((notPass!=0) && (number_person>save_person)){
		doorlock.write(95);
		t.stop(notPass);
		notPass=0;
	}
}

void sensing_tissue(){
	tissue_sens = digitalRead(tissue_pin);
	//tissue_sens를 반전시킨 값으로 LED를 온/오프 (휴지가 부족한 경우 tissue_sens값이 0)
	digitalWrite(tissue_led,tissue_sens==1?0:1);
}

void sensing_dht(){
	humidity = dht.readHumidity();  //습도
	temperature = dht.readTemperature();  //온도 (C)
}

//입구센서 센싱
void sensing_door(char sensor_inner, char sensor_outter){	
	int Sensor_inner = analogRead(sensor_inner);
	int Sensor_outter = analogRead(sensor_outter);
	float cm_inner = 2076/(Sensor_inner-11);	//그래프를 수식화한 식
	float cm_outter = 2076/(Sensor_outter-11);
	//Serial.print(cm_outter); Serial.print("  "); Serial.println(cm_inner); 
	//안쪽센서 센서범위 내 인 경우
	if(cm_inner<distance_enter)
		door_sens[0] = 1;
	else
		door_sens[0] = 0;
	
	//바깥쪽센서 센서범위 내 인 경우
	if(cm_outter<distance_enter)
		door_sens[1] = 1;
	else
		door_sens[1] = 0;
}

//입구센서 카운팅 함수 (센싱 되어있어야 함)
void doorCount(){	
	static bool isin,isout=false;
	static unsigned long prev_time=0;
	if(current_time-prev_time>4000){	//4초이상 센싱 없을 경우
		isin = isout = false;	//값 초기화
	}
	//안쪽센서 인식시
	if(door_sens[1]==1){
		if(current_time-prev_time>interval){
			prev_time=current_time;	//딜레이
			if(isin){	//바깥쪽센서 먼저 인식된 경우
				number_person++;
				Serial.println(number_person);
				isin = false;
			}
			//먼저 인식된 센서 없을 때
			else if(!isin){
				isout = true;
			}
		}
	}
	//바깥쪽센서 인식시
	else if(door_sens[0]==1){
		if(current_time-prev_time>interval){
			prev_time=current_time;	//딜레이
			if(isout == true){	//안쪽센서 먼저 인식된 경우
				if(number_person>0){
					number_person--;
					Serial.println(number_person);
					isout = false;
				}
				isout = false;
			}
			//먼저 인식된 센서 없을 때
			else if(!isout){
				isin = true;
			}
		}
    }
}
// ----------------------------------센서부끝----------------------------------