#include <Servo.h>

#include <Max3421e.h>
#include <Usb.h>
#include <AndroidAccessory.h>


//TODO: definir padrao de cores conforme ativacao dos servos
//TODO: detectar conexao e ativar leds/servos
//OK: detectar desconexao e desativar leds/servos



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

#define  RED            3
#define  GREEN          2
#define  BLUE           1

#define  POSITION_ON    0
#define  POSITION_OFF   30


AndroidAccessory acc("luisleao",
  "CaipirinhaHero",
  "Caipirinha Hero Player",
  "1.0",
  "http://luisleao.com.br/",
  "0000000000000001");


Servo servos[TOTAL_SERVOS];
boolean conectado = false;


void setup();
void loop();


void setup()
{
  // init servos
  for (int i=0; i<TOTAL_SERVOS; i++) {
    servos[i].attach(2+i);
    servos[i].write(POSITION_OFF);
  }

  // accessory power on
  acc.powerOn();
}

void loop()
{
  byte msg[2];

  if (acc.isConnected()) {
    if (conectado == false) {
      //TODO: aviso de ativacao da conexao
      conectado = true;
    }
    
    int len = acc.read(msg, sizeof(msg), 1);
    int total_active;
    int servo_bit;
    
    if (len > 0) {
      if (msg[0] == 0x1) {
        for (int i=0; i<8; i++) {
          servo_bit = bitRead(msg[1], 0);
          if (servo_bit == 1) {
            total_active++;
            servos[i].write(POSITION_ON);
          } else {
            servos[i].write(POSITION_OFF);
          }
        }

        //TODO: definir cores dos leds conforme as notas
        //analogWrite(LED1_GREEN, 255 - msg[2]);
        analogWrite(RED, 255);
        analogWrite(GREEN, 255);
        analogWrite(BLUE, 255);

      }
    }

  } 
  else {
    // desligar leds e voltar servos para posicao padrao
    conectado = false;
    
    analogWrite(RED, 255);
    analogWrite(GREEN, 255);
    analogWrite(BLUE, 255);

    for (int i=0; i<8; i++) {
      servos[i].write(POSITION_OFF);
    }

  }

  delay(10);
}


