package com.monkey.automation.monkey_automation_service.controllers.entities.others;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LedStripRequest {

    private Integer red;

    private Integer green;

    private Integer blue;
}
