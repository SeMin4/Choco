#include <LiquidCrystal_I2C.h>//LCD 헤더파일
#include <Wire.h>//LCD 헤더파일
#include <SoftwareSerial.h>   //Software Serial Port

#define RxD         7
#define TxD         6

#define PINLED      9
#define speakerPin  5

#define LEDON()     digitalWrite(PINLED, HIGH)
#define LEDOFF()    digitalWrite(PINLED, LOW)

#define DEBUG_ENABLED  1

SoftwareSerial blueToothSerial(RxD,TxD); 
LiquidCrystal_I2C lcd(0x27, 16, 2);

void setup()
{
    lcd.begin();
    lcd.backlight();
    Serial.begin(9600);
    pinMode(RxD, INPUT);
    pinMode(TxD, OUTPUT);
    pinMode(PINLED, OUTPUT);
    pinMode(speakerPin, OUTPUT);
    LEDOFF();
    
    setupBlueToothConnection();
}

void loop()
{
    char recvChar;
    int i=0;
    String message;
    
    if(blueToothSerial.available()) //블루투스통해 값이 있다면
    {
        digitalWrite(speakerPin, HIGH); //부저에서 소리나게하기
        
        while(blueToothSerial.available()>0){//받아오는 글 lcd 출력
              lcd.print(Serial.read());
        }
        lcd.blink();//커서 깜빡이게하기
            //recvChar = blueToothSerial.read();
            //if(recvChar == '1')//어플에서 return값이 1이면{

        for(i=0;i<100;i++){ //값이 들어오면 9번 LED모듈에서 100번깜빡이기
              LEDON();
              delay(500); //0.5초 딜레이
              LEDOFF();
        }     
            /*}
            else if(recvChar == '0')
            {
                LEDOFF();
            }*/
    }
    digitalWrite(speakerPin, LOW); //부저 끄기
}

void setupBlueToothConnection()
{
    blueToothSerial.begin(38400);                           // Set BluetoothBee BaudRate to default baud rate 38400
    blueToothSerial.print("\r\n+STWMOD=0\r\n");             // set the bluetooth work in slave mode
    blueToothSerial.print("\r\n+STNA=SeeedBTSlave\r\n");    // set the bluetooth name as "SeeedBTSlave"
    blueToothSerial.print("\r\n+STOAUT=1\r\n");             // Permit Paired device to connect me
    blueToothSerial.print("\r\n+STAUTO=0\r\n");             // Auto-connection should be forbidden here
    delay(2000);                                            // This delay is required.
    blueToothSerial.print("\r\n+INQ=1\r\n");                // make the slave bluetooth inquirable
    Serial.println("The slave bluetooth is inquirable!");
    delay(2000);                                            // This delay is required.
    blueToothSerial.flush();
}
