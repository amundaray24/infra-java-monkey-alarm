package com.monkey.automation.monkey_automation_service.controllers;

import com.monkey.automation.monkey_automation_service.controllers.entities.alarm.AlarmStatus;
import com.monkey.automation.monkey_automation_service.processors.lcd.LcdDisplayController;
import com.monkey.automation.monkey_automation_service.services.AutomationService;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalState;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@AllArgsConstructor
public class AlarmController implements ApplicationListener<ApplicationReadyEvent> {

    DigitalInput checkAlarmButton;

    DigitalInput movementSensor;

    AutomationService automationService;

    LcdDisplayController lcdDisplayController;


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        lcdDisplayController.updateStatus(AlarmStatus.DISARMED);

        checkAlarmButton.addListener(digitalStateChangeEvent -> {
            if (digitalStateChangeEvent.state() == DigitalState.LOW){
                automationService.alarmStatus();
            }
        });

        movementSensor.addListener(digitalStateChangeEvent -> {
            if (digitalStateChangeEvent.state() == DigitalState.LOW){
                automationService.dispatchAlarm();
            }
        });
    }
}
