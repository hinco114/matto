/*********************************************************************
Matto Project
2016-09-23

시리얼 통신 프로토콜
아두이노가 읽는 방법 [명령어,값]
아두이노가 보내는 방법 [상태,값1이름,값1,값2이름,값2]
에러의 경우 [F,REASON,이유]

적용된 명령어
1. DOPEN = 문 열기
2. DHT = 온도 습도 정보 보내기 [tem,온도,hum,습도]
3. TISSUE = 휴지센서 값 받기

핀번호 매칭 다시 해야함.
DoorOpen 함수 구현 필요
Timer 라이브러리 사용 여부(편할듯)

*********************************************************************/

// ----------------------------------선언부----------------------------------

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

//조건들
int interval_temp = 2000;	//온습도센서 카운트 딜레이
int interval = 700;		//입구센서 카운트 딜레이
float distance_enter = 30.0;

//핀 셋팅
int tissue_pin=4;		//휴지센서
int tissue_led=5;		//휴지 LED
char door_inner_pin=A0;		//입구 안쪽센서
char door_outter_pin=A1;	//입구 바깥쪽센서
int doorlock_pin=3;		//도어락(서보모터)


//센서값
float door_sens[2];
int tissue_sens;
int sector_sens[2];
int number_person=0;

//통신에 필요한 변수
String in_data="";
bool needCommu=false;
// ----------------------------------선언부끝----------------------------------

// ----------------------------------메인부----------------------------------
void setup(){
	Serial.begin(9600);
	dht.begin();	//온습도센서 구현. 센싱에 250ms 소요
	doorlock.attach(doorlock_pin);	//도어락 연결
	pinMode(tissue_led,OUTPUT);
}

void loop(){
	current_time = millis();
	sensing_door(door_inner_pin,door_outter_pin);	//입구센서 센싱
	doorCount();	//센싱값으로 인원처리
	sensing_tissue();	//휴지센서 처리
	if(Serial.available()){
		fromSerial();	//시리얼통신으로 받을게 있으면 커뮤니케이션 함수 실행
	}
}

// ----------------------------------메인부끝---------------------------------


// ----------------------------------통신부----------------------------------
// [상태,값1이름,값1,값2이름,값2]형태로 시리얼 전송
// 값1이름이나 값2이름이이 "0"인 경우 
// 상태가 0으로 동작이 실패한 경우, 값1이름으로 REASON을 받고 [status,REASON,내용] 전송
void toSerial(int status,String dataName1,int data1,String dataName2,int data2){
	char s = status==1?'S':'F';
	Serial.print("[");
	Serial.print(s);
	if(dataName1=="0"){		//dataName1 가 "0"이면 끝냄
		Serial.println("]");
		return;
	}
	Serial.print(",");
	Serial.print(dataName1);
	Serial.print(",");
	if (dataName1=="REASON"){	//값1이름이 REASON인 경우
		Serial.print(dataName2);
		Serial.println("]");
		return;
	}
	Serial.print(data1);
	if(dataName2 == "0"){	//dataName2 가 "0"이면 끝냄
		Serial.println("]");
		return;
	}
	Serial.print(",");
	Serial.print(dataName2);
	Serial.print(",");
	Serial.print(data2);
	Serial.println("]");
}

void fromSerial(){	//통신관련 함수
	char data = Serial.read();	//일단 읽음
	if(needCommu){			//읽는 중이었다면
		if(data == ']'){	//마지막을 알리는 경우 설정갑 초기화와 함께 파싱 진행
			parseData(in_data);
			needCommu = false;
			in_data="";
		}
		else{
			in_data += data;	//내용의 중간인 경우 in_data에 계속 추가
		}
	}
	else{
		if(data == '['){
			needCommu = true;	//내용의 시작을 알리면 읽기 모드 돌입
		}
	}
}

void parseData(String needParse){
	String cmd = needParse.substring(0,needParse.indexOf(','));
	String data = needParse.substring(needParse.indexOf(',')+1);
	if(cmd == "DOPEN"){
		// doorOpen();
		doorlock.write(data.toInt());
		
		toSerial(1,"0",0,"0",0);
	}
	else if(cmd == "DHT"){	//DHT명령인 경우 온습도 센싱 실행후 반환
		sensing_dht();
		if (isnan(humidity) || isnan(temperature)) {
			toSerial(0,"REASON",0,"Failed to read from DHT sensor",0); 
		}
		else { 
			toSerial(1,"TEM",temperature,"HUM",humidity);//시리얼 통신에 보냄
		}
	}
	else if(cmd == "TISSUE"){	//TISSUE명령어인 경우 tissue_sens값 반환
		toSerial(1,"Tissue Data",tissue_sens,"0",0);
	}
}
// ----------------------------------통신부끝----------------------------------


// ----------------------------------센서부----------------------------------

// Delay 없이 타이머를 이용하고, 일정시간 지나면 문이 잠기도록 해야함
void doorOpen(){
	doorlock.write(21);
	delay(1500);
	doorlock.write(95);
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
	float cm_inner = 10650.08 * pow(Sensor_inner,-0.935) - 10;	//그래프를 수식화한 식
	float cm_outter = 10650.08 * pow(Sensor_outter,-0.935) - 10;
	
	//안쪽센서 센서범위 내 인 경우
	if(cm_inner<distance_enter)
		door_sens[0] = 1;
	else
		door_sens[0] = 0;
	
	//바깥쪽센서 센서범위 내 인 경우
	if((cm_outter<distance_enter))
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