/*
Matto Project
2016-09-03
온습도센서 모두 정상 작동.
입구센서 실제 실험 필요. 딜레이 정상 작동.

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

//조건들
int interval_temp = 2000;	//카운트후 다음 센싱 딜레이
int interval = 700;
float distance_enter = 30.0;

//핀 셋팅
int tissue_pin[2]={3,4};		//휴지센서
char door_inner_pin=A0;		//입구 안쪽센서
char door_outter_pin=A1;	//입구 바깥쪽센서

//센서값
float door_sens[2];
int tissue_sens[2];
int sector_sens[2];
int number_person=0;
String in_data="";


void setup(){
	Serial.begin(9600);
	dht.begin();	//온습도센서 구현. 센싱에 250ms 소요
}

void loop(){
	current_time = millis();
	sensing_door(door_inner_pin,door_outter_pin);
	doorcount();
	if(current_time-prev_time>interval_temp){
			prev_time=current_time;
			sensing_dht();
			Serial.println(temperature);
	}
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
	humidity = dht.readHumidity();  //습도
	temperature = dht.readTemperature();  //온도 (C)
	if (isnan(humidity) || isnan(temperature)) {
		Serial.println("Failed to read from DHT sensor!");
		return;
	}
}

void sensing_door(char sensor_inner, char sensor_outter){	//입구센서 센싱
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

void doorcount(){	//입구센서 카운팅 함수 (센싱 되어있어야 함)
	static int isin,isout={0};
	static unsigned long prev_time=0;
	/*
	Serial.print("Sens Value 1 : ");
	Serial.print(door_sens[0]);
	Serial.print("  Value 2 : ");
	Serial.println(door_sens[1]);
	*/
	//안쪽센서 인식시
	if(current_time-prev_time>4000){	//4초이상 센싱 없을 경우
		isin = isout = 0;	//값 초기화
	}
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