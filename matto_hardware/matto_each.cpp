/*
Matto Project

*/
unsigned long current_time=0;
unsigned long prev_time=0;
int interval = 500;

//핀 셋팅
int tissue_pin[2]={3,4};
int door_sensing_pin[2]={5,6};
int sector_sensing_pin[2]={7,8};

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
}

void loop(){
	current_time = millis();
	sensing_door();
	doorcount();
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

void sensing_door(){
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

void doorcount(){
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