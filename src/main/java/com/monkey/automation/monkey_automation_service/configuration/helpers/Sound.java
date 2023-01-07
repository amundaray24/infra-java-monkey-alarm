package com.monkey.automation.monkey_automation_service.configuration.helpers;

import com.monkey.automation.monkey_automation_service.configuration.helpers.enums.BuzzerNote;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class Sound {

    private BuzzerNote note;
    Integer beats;
}
