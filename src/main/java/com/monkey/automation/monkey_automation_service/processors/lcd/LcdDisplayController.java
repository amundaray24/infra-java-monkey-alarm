package com.monkey.automation.monkey_automation_service.processors.lcd;

import com.monkey.automation.monkey_automation_service.controllers.entities.alarm.AlarmStatus;

public interface LcdDisplayController {

    void updateStatus (AlarmStatus alarmStatus);

    void statusAlreadySet (AlarmStatus alarmStatus);

    void dispatchedAlarm ();
}
