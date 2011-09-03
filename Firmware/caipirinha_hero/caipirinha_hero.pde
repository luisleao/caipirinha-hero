
#include <Max3421e.h>
#include <Usb.h>
#include <AndroidAccessory.h>
#include <TimedAction.h>
#include <Servo.h>


//TODO: definir padrao de cores conforme ativacao dos servos
//TODO: detectar conexao e ativar leds/servos



/*
 * Protocolo de Comunicacao
 *
 * 0x01: xxx (output)
 * 0x02: xxx (output)
 * 0x03: xxx (input)
 * 0x04: xxx (input)
 *
*/




#define  TOTAL_SERVOS   8



#define TEMPO 100
#define TOTAL_SERVOS 8
#define ANGULO_ON 15 //45
#define ANGULO_OFF 0 //60

#define SERVO_PINO 5



AndroidAccessory acc("luisleao",
  "CaipirinhaHero",
  "Caipirinha Hero Player",
  "1.0",
  "http://luisleao.com.br/",
  "0000000000000001");


Servo servos[TOTAL_SERVOS];
boolean conectado = false;



TimedAction t_reset_servo_0 = TimedAction(TEMPO, reset_servo_0);
TimedAction t_reset_servo_1 = TimedAction(TEMPO, reset_servo_1);
TimedAction t_reset_servo_2 = TimedAction(TEMPO, reset_servo_2);
TimedAction t_reset_servo_3 = TimedAction(TEMPO, reset_servo_3);
TimedAction t_reset_servo_4 = TimedAction(TEMPO, reset_servo_4);
TimedAction t_reset_servo_5 = TimedAction(TEMPO, reset_servo_5);
TimedAction t_reset_servo_6 = TimedAction(TEMPO, reset_servo_6);
TimedAction t_reset_servo_7 = TimedAction(TEMPO, reset_servo_7);






void setup();
void loop();


void setup()
{
  reset_servo_0();
  reset_servo_1();
  reset_servo_2();
  reset_servo_3();
  reset_servo_4();
  reset_servo_5();
  reset_servo_6();
  reset_servo_7();


  Serial.begin(9600);

  // accessory power on
  acc.powerOn();
  
}

void loop()
{

  t_reset_servo_0.check();
  t_reset_servo_1.check();
  t_reset_servo_2.check();
  t_reset_servo_3.check();
  t_reset_servo_4.check();
  t_reset_servo_5.check();
  t_reset_servo_6.check();
  t_reset_servo_7.check();
  
  byte msg[3];

  if (acc.isConnected()) {
    if (conectado == false) {
      //TODO: aviso de ativacao da conexao
      for (int i=0; i<8; i++) {
        servos[i].attach(SERVO_PINO+i);
        servos[i].write(ANGULO_OFF);
      }
      
      conectado = true;
    }
    
    int len = acc.read(msg, sizeof(msg), 3);
    int total_active;
    int servo_bit;
    
    if (len > 0) {
      if (msg[0] == 0x1) {
        for (int i=0; i<8; i++) {
          servo_bit = bitRead(msg[2], i);
          if (servo_bit == 1) { //msg[i+1]
            total_active++;
            //servos[i].attach(SERVO_PINO+i);
            servos[i].write(ANGULO_ON);
            
            Serial.print("ATIVOU ");
            Serial.println(i+1);
            
            switch(i) {
              case 0: t_reset_servo_0.reset(); t_reset_servo_0.enable(); break;
              case 1: t_reset_servo_1.reset(); t_reset_servo_1.enable(); break;
              case 2: t_reset_servo_2.reset(); t_reset_servo_2.enable(); break;
              case 3: t_reset_servo_3.reset(); t_reset_servo_3.enable(); break;
              case 4: t_reset_servo_4.reset(); t_reset_servo_4.enable(); break;
              case 5: t_reset_servo_5.reset(); t_reset_servo_5.enable(); break;
              case 6: t_reset_servo_6.reset(); t_reset_servo_6.enable(); break;
              case 7: t_reset_servo_7.reset(); t_reset_servo_7.enable(); break;
            }

          //} else {
          //  servos[i].write(ANGULO_OFF);
          }
          
        }
        
      }
    }

  } 
  else {
    // desligar leds e voltar servos para posicao padrao
    conectado = false;
    
    for (int i=0; i<8; i++) {
      servos[i].write(ANGULO_OFF);
      servos[i].detach();
    }

  }

  delay(10);
}




void reset_servo(int servo) {
  
  if (servo > TOTAL_SERVOS - 1)
    return;

  servos[servo].write(ANGULO_OFF);
  //servos[servo].detach();

}

void reset_servo_0() { t_reset_servo_0.disable(); reset_servo(0); }
void reset_servo_1() { t_reset_servo_1.disable(); reset_servo(1); }
void reset_servo_2() { t_reset_servo_2.disable(); reset_servo(2); }
void reset_servo_3() { t_reset_servo_3.disable(); reset_servo(3); }
void reset_servo_4() { t_reset_servo_4.disable(); reset_servo(4); }
void reset_servo_5() { t_reset_servo_5.disable(); reset_servo(5); }
void reset_servo_6() { t_reset_servo_6.disable(); reset_servo(6); }
void reset_servo_7() { t_reset_servo_7.disable(); reset_servo(7); }



