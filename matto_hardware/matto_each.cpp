/*
Matto Project
2016-08-26
온습도센서 추가
핀번호 매칭 다시 해야함.
현재 딜레이 없이 루프 돔.
*/

//온습도센서
#include "DHT.h"	//온습도센서 라이브러리 (DHT22)
#define DHTPIN 2     //센서 핀번호
#define DHTTYPE DHT22   // DHT 22  (AM2302), AM2321
DHT dht(DHTPIN, DHTTYPE);
float humidity;
float temperature;

//시간을 위한 변수
unsigned long current_time=0;
unsigned long prev_time=0;
int interval = 500;	//센싱 유지 간격

//핀 셋팅
int tissue_pin[2]={3,4};		//휴지센서
int door_sensing_pin[2]={5,6};		//입구센서
int sector_sensing_pin[2]={7,8};		//칸센서

//센서값
int door_sens[4];
int tissue_sens[2];
int sector_sens[2];
int number_person=0;
String in_data="";


void setup(){
	Serial.begin(9600);
	Serial1.begin(9600);
	for(int i=5;i<7;i++){
		pinMode(sector_sensing_pin[i],INPUT);
	}
	dht.begin();	//온습도센서 구현. 센싱에 250ms 소요
}

void loop(){
	current_time = millis();
	sensing_door();
	doorcount();
	sensing_dht();
	//communicate();
	//Serial.println(number_person);
}

/*
void communicate(){	//통신관련 함수
	if(Serial1.available()){	//Serial1 버퍼에 자료있으면
		while(Serial1.available()){	//모두 받아서 in_data에 넣음
			in_data += (char)Serial1.read(); 
		}
	}
}
*/

void sensing_dht(){
	humidity = dht.readHumidity();	//습도
	temperature = dht.readTemperature();	//온도 (C)
	if (isnan(h) || isnan(t)) {
		Serial.println("Failed to read from DHT sensor!");
		return;
  	}
}

void sensing_door(){	//입구센서 센싱
	//0,1 -> 입구쪽, 안쪽
	for(int i=0;i<2;i++){
		door_sens[i]=digitalRead(door_sensing_pin[i]);
	}
	/*
	Serial.print("test for sens 0 :");
	Serial.print(door_sens[0]);
	Serial.print("sens 1 :");
	Serial.println(door_sens[1]);
	*/
}

void doorcount(){	//입구센서 카운팅 함수 (센싱 되어있어야 함)
	static int isin,isout={0};
	//안쪽센서 인식시
	if(door_sens[1]==1){
		if(current_time-prev_time>interval){
			prev_time=current_time;	//딜레이
			if(isin == 1){	//바깥쪽센서 먼저 인식된 경우
				number_person++;
				Serial.println(number_person);
				isin = 0;
			}
			//먼저 인식된 센서 없을 때
			else if(!isin){
				isout = 1;
			}
		}
	}
	//바깥쪽센서 인식시
	else if(door_sens[0]==1){
		if(current_time-prev_time>interval){
			prev_time=current_time;	//딜레이
			if(isout == 1){	//안쪽센서 먼저 인식된 경우
				if(number_person>0){
					number_person--;
					Serial.println(number_person);
					isout = 0;
				}
				isout = 0;
			}
			//먼저 인식된 센서 없을 때
			else if(!isout){
				isin = 1;
			}
		}
    }
}