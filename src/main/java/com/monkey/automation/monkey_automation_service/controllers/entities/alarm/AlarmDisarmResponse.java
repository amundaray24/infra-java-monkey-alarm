package com.monkey.automation.monkey_automation_service.controllers.entities.alarm;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlarmDisarmResponse {

    private AlarmStatus status;

    private Date disarmDate;

    private Boolean notification;
    
}
