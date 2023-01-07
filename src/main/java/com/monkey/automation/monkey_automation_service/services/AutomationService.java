package com.monkey.automation.monkey_automation_service.services;

import com.monkey.automation.monkey_automation_service.controllers.entities.alarm.*;

public interface AutomationService {

    AlarmArmResponse alarmArm (AlarmArmRequest alarmArmRequest);

    AlarmDisarmResponse alarmDisarm (AlarmDisarmRequest alarmDisarmRequest);

    AlarmStatusResponse alarmStatus ();

    void dispatchAlarm ();

/*    ToggleLedResponse toggleLed();

    void colorStripLed (LedStripRequest ledStripRequest);

    void moveServo (Integer percentage);*/


}
