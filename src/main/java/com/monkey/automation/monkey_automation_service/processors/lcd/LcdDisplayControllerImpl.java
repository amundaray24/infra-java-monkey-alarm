package com.monkey.automation.monkey_automation_service.processors.lcd;

import com.monkey.automation.monkey_automation_service.configuration.entities.StatusControllers;
import com.monkey.automation.monkey_automation_service.configuration.helpers.LcdDisplay;
import com.monkey.automation.monkey_automation_service.controllers.entities.alarm.AlarmStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class LcdDisplayControllerImpl implements LcdDisplayController {

    Map<StatusControllers,Boolean> dispatchController;

    LcdDisplay lcdDisplay;

    @Async
    @Override
    public void updateStatus (AlarmStatus alarmStatus){
        lcdDisplay.clearDisplay(false);
        lcdDisplay.displayText(String.format("%14s","ALARM STATUS:"),1);
        lcdDisplay.displayText(buildStatus(alarmStatus),2);
        delay(2500);
        lcdDisplay.clearDisplay(true);
    }

    @Async
    @Override
    public void statusAlreadySet(AlarmStatus alarmStatus) {
        lcdDisplay.clearDisplay(false);
        lcdDisplay.displayText("ALARM IS ALREADY",1);
        lcdDisplay.displayText(buildStatus(alarmStatus),2);
        delay(2500);
        lcdDisplay.clearDisplay(true);
    }

    @Async
    @Override
    public void dispatchedAlarm () {
        lcdDisplay.clearDisplay(false);
        lcdDisplay.displayText(" ALARM IS FIRED",1);
        lcdDisplay.displayText(String.format("%-16s",String.format("%12s","!!ALERT!!")),2);
    }

    protected String buildStatus (AlarmStatus alarmStatus) {
        switch (alarmStatus){
            case DISARMED:
                return String.format("%12s",alarmStatus.getStatus());
            case ARMED:
            default:
                return String.format("%10s",alarmStatus.getStatus());
        }
    }

    protected void delay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
