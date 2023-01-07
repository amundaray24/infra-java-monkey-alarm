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
public class AlarmArmResponse {

    private AlarmStatus status;

    private Date armDate;

    private Boolean alert;

    private Boolean silenced;

    private Boolean notification;
    
}
