package com.monkey.automation.monkey_automation_service.configuration.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum StatusControllers {

    DISPATCHED_CONTROL("DISPATCHED_CONTROL"),
    ARMED_CONTROL("ARMED_CONTROL");

    private final String statusControl;
}
