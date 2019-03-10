#include "CurieIMU.h"
#include <CurieBLE.h>
const int trigger = 10;
const int echo = 9;

long duration;
int distance; 
int oldDistance = 0; // to check for any changes occurring
unsigned long interruptsTime = 0;    // get the time when motion event is detected

BLEPeripheral blePeripheral; // The board we're programming. 
BLEService distanceService("180D");

BLECharacteristic distanceChar("2A37", BLERead | BLENotify, 2);



void setup() {
  // put your setup code here, to run once:
  pinMode(trigger, OUTPUT);

  pinMode(echo, INPUT);

  Serial.begin(9600);
  BLE.begin();

  // Setting local name for BLE device
  blePeripheral.setLocalName("DistanceSketch");
  blePeripheral.setAdvertisedServiceUuid(distanceService.uuid());

  blePeripheral.addAttribute(distanceService);
  blePeripheral.addAttribute(distanceChar);

  blePeripheral.begin();
  Serial.println("Bluetooth device active, waiting for connections...");

    /* Initialise the IMU */
  CurieIMU.begin();
  CurieIMU.attachInterrupt(eventCallback);

  /* Enable Motion Detection */
  CurieIMU.setDetectionThreshold(CURIE_IMU_MOTION, 20); // 20mg
  CurieIMU.setDetectionDuration(CURIE_IMU_MOTION, 10);  // trigger times of consecutive slope data points
  CurieIMU.interrupts(CURIE_IMU_MOTION);

}

void loop() {

  BLECentral central = blePeripheral.central();

  if (central) {
    Serial.print("Connected to central: ");
    Serial.println(central.address());
  }

while (central.connected()) {
   digitalWrite(trigger, LOW);
 delayMicroseconds(1000000); 
 //Sets the trigPin on HIGH state for 10 micro seconds 
 digitalWrite(trigger, HIGH); 
 delayMicroseconds(1000000); 
 digitalWrite(trigger, LOW); 
 // Reads the echoPin, returns the sound wave travel time in microseconds 
 duration = pulseIn(echo, HIGH); 
 // Calculating the distance 
 distance= duration*0.034/2; 
 // Prints the distance on the Serial Monitor 
 Serial.print("Distance: "); 
 Serial.println(distance);
 if (true || oldDistance - distance >= 2) {
  oldDistance = distance;
  updateDistance();
 }
}
    delayMicroseconds(1000000); 
    Serial.print("Disconnected from central: ");
    Serial.println(central.address());
  
}

void updateDistance() {
  Serial.print("Distance is now: ");
  Serial.println(distance);
  const unsigned char distanceCharArray[1] = { (char)distance };
  distanceChar.setValue(distanceCharArray, 1);
  oldDistance = distance;
}


static void eventCallback(void){
  if (CurieIMU.getInterruptStatus(CURIE_IMU_MOTION)) {
    if (CurieIMU.motionDetected(X_AXIS, POSITIVE))
      Serial.println("Negative motion detected on X-axis");
    if (CurieIMU.motionDetected(X_AXIS, NEGATIVE))
      Serial.println("Positive motion detected on X-axis");
    if (CurieIMU.motionDetected(Y_AXIS, POSITIVE))
      Serial.println("Negative motion detected on Y-axis");
    if (CurieIMU.motionDetected(Y_AXIS, NEGATIVE))
      Serial.println("Positive motion detected on Y-axis");
    if (CurieIMU.motionDetected(Z_AXIS, POSITIVE))
      Serial.println("Negative motion detected on Z-axis");
    if (CurieIMU.motionDetected(Z_AXIS, NEGATIVE))
      Serial.println("Positive motion detected on Z-axis");
    interruptsTime = millis(); 
  } 
}
