package com.monkey.automation.monkey_automation_service.processors.buzzer;

import com.monkey.automation.monkey_automation_service.configuration.entities.StatusControllers;
import com.monkey.automation.monkey_automation_service.configuration.helpers.SimpleDigital;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class BuzzerControllerImpl implements BuzzerController {

    Map<StatusControllers,Boolean> dispatchController;

    SimpleDigital buzzerAlarm;

    @Async
    @Override
    public void armAlarm() {
        buzzerAlarm.off();
        delay(100);
        buzzerAlarm.on();
    }

    @Async
    @Override
    public void disarmAlarm() {
        dispatchController.put(StatusControllers.DISPATCHED_CONTROL,Boolean.FALSE);
        buzzerAlarm.off();
        delay(120);
        buzzerAlarm.on();
        delay(60);
        buzzerAlarm.off();
        delay(120);
        buzzerAlarm.on();
    }

    @Async
    @Override
    public void activeAlarm() {
        while (dispatchController.get(StatusControllers.DISPATCHED_CONTROL)) {
            buzzerAlarm.off();
            delay(500);
            buzzerAlarm.on();
            delay(500);
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
