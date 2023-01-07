package com.monkey.automation.monkey_automation_service.services;

import com.monkey.automation.monkey_automation_service.configuration.entities.StatusControllers;
import com.monkey.automation.monkey_automation_service.controllers.entities.alarm.*;
import com.monkey.automation.monkey_automation_service.processors.buzzer.BuzzerController;
import com.monkey.automation.monkey_automation_service.processors.lcd.LcdDisplayController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Slf4j
@Service("automationService")
@AllArgsConstructor
public class AutomationServiceImpl implements AutomationService {

   Map<StatusControllers,Boolean> dispatchController;

   LcdDisplayController lcdDisplayController;

   BuzzerController buzzerController;

   //BBDD

   @Override
   public AlarmArmResponse alarmArm(AlarmArmRequest alarmArmRequest) {
      AlarmArmResponse alarmArmResponse = new AlarmArmResponse();
      alarmArmResponse.setArmDate(new Date());
      alarmArmResponse.setNotification(Boolean.FALSE);
      alarmArmResponse.setNotification(Boolean.FALSE);
      if (dispatchController.get(StatusControllers.DISPATCHED_CONTROL)){
         alarmArmResponse.setStatus(AlarmStatus.FIRED);
         return alarmArmResponse;
      }
      if (dispatchController.get(StatusControllers.ARMED_CONTROL)){
         lcdDisplayController.statusAlreadySet(AlarmStatus.ARMED);
         log.info("Alarm is already armed");
      } else {
         dispatchController.put(StatusControllers.ARMED_CONTROL,Boolean.TRUE);
         lcdDisplayController.updateStatus(AlarmStatus.ARMED);
         if (alarmArmRequest.getAlert()) buzzerController.armAlarm();
         log.info("Alarm armed");
      }
      alarmArmResponse.setArmDate(new Date());
      alarmArmResponse.setStatus(AlarmStatus.ARMED);
      alarmArmResponse.setNotification(alarmArmRequest.getNotification());
      alarmArmResponse.setSilenced(alarmArmRequest.getSilenced());
      return alarmArmResponse;
   }

   @Override
   public AlarmDisarmResponse alarmDisarm(AlarmDisarmRequest alarmDisarmRequest) {
      AlarmDisarmResponse alarmDisarmResponse = new AlarmDisarmResponse();
      if (!dispatchController.get(StatusControllers.ARMED_CONTROL)){
         lcdDisplayController.statusAlreadySet(AlarmStatus.DISARMED);
         log.info("Alarm is already disarmed");
      } else {
         dispatchController.put(StatusControllers.ARMED_CONTROL,Boolean.FALSE);
         lcdDisplayController.updateStatus(AlarmStatus.DISARMED);
         if (alarmDisarmRequest.getAlert()) buzzerController.disarmAlarm();
         log.info("Alarm disarmed");
      }
      alarmDisarmResponse.setDisarmDate(new Date());
      alarmDisarmResponse.setStatus(AlarmStatus.DISARMED);
      alarmDisarmResponse.setNotification(alarmDisarmRequest.getNotification());
      return alarmDisarmResponse;
   }

   @Override
   public AlarmStatusResponse alarmStatus() {
      AlarmStatusResponse alarmStatusResponse = new AlarmStatusResponse();
      alarmStatusResponse.setStatusDate(new Date());
      alarmStatusResponse.setNotification(Boolean.FALSE);
      alarmStatusResponse.setNotification(Boolean.FALSE);
      if (dispatchController.get(StatusControllers.DISPATCHED_CONTROL)){
         alarmStatusResponse.setStatus(AlarmStatus.FIRED);
         return alarmStatusResponse;
      }
      if (!dispatchController.get(StatusControllers.ARMED_CONTROL)){
         lcdDisplayController.updateStatus(AlarmStatus.DISARMED);
         alarmStatusResponse.setStatus(AlarmStatus.DISARMED);
      }else {
         lcdDisplayController.updateStatus(AlarmStatus.ARMED);
         alarmStatusResponse.setStatus(AlarmStatus.ARMED);
      }
      return alarmStatusResponse;
   }

   @Override
   public void dispatchAlarm() {
      if (dispatchController.get(StatusControllers.ARMED_CONTROL) && !dispatchController.get(StatusControllers.DISPATCHED_CONTROL)){
         log.info("-------ALARM IDSPATCH {} ------", new Date().toString());
         dispatchController.put(StatusControllers.DISPATCHED_CONTROL,Boolean.TRUE);
         buzzerController.activeAlarm();
         lcdDisplayController.dispatchedAlarm();
      }
   }


/*
    LedStrip ledStrip;

    ServoMotor servoMotor;*/

/*    @Override
    public ToggleLedResponse toggleLed() {
        log.info("-----INICIO SERVICIO toggleLed -----");
        ToggleLedResponse toggleLedResponse = new ToggleLedResponse();
        toggleLedResponse.setOperationDate(new Date());
        toggleLedResponse.setStatus(!simpleDigital.toggleState());
        log.info("-----FIN SERVICIO toggleLed ----- {}",toggleLedResponse );
        return toggleLedResponse;
    }

    @Override
    public void colorStripLed(LedStripRequest ledStripRequest) {
        log.info("-----INICIO SERVICIO colorStripLed -----");
        ledStrip.allOff();
        ledStrip.setStripColor(PixelColor.createColorRGB(ledStripRequest.getRed(),ledStripRequest.getGreen(),ledStripRequest.getBlue()));
        ledStrip.render();
        log.info("-----FIN SERVICIO colorStripLed -----");
    }

    @Override
    public void moveServo(Integer percentage) {
        log.info("-----INICIO SERVICIO moveServo -----");
        log.info("SERVO MAX ANGLE {}", servoMotor.getMaxAngle());
        servoMotor.setPercent(percentage);
        log.info("-----FIN SERVICIO moveServo -----");
    }*/
}
