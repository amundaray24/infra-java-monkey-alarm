package com.monkey.automation.monkey_automation_service.processors.buzzer;

import com.monkey.automation.monkey_automation_service.configuration.helpers.SimpleDigital;

public interface BuzzerController {

    void armAlarm ();
    void disarmAlarm ();

    void activeAlarm ();
}

