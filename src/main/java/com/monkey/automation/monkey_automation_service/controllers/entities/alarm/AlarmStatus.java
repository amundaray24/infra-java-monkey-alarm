package com.monkey.automation.monkey_automation_service.controllers.entities.alarm;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public enum AlarmStatus {
    FIRED("FIRED"),
    ARMED("ARMED"),
    DISARMED("DISARMED");

    private final String status;
}
