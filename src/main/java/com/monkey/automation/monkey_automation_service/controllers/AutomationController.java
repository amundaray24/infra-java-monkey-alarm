package com.monkey.automation.monkey_automation_service.controllers;

import com.monkey.automation.monkey_automation_service.controllers.entities.alarm.AlarmArmRequest;
import com.monkey.automation.monkey_automation_service.controllers.entities.alarm.AlarmArmResponse;
import com.monkey.automation.monkey_automation_service.controllers.entities.alarm.AlarmDisarmRequest;
import com.monkey.automation.monkey_automation_service.controllers.entities.alarm.AlarmDisarmResponse;
import com.monkey.automation.monkey_automation_service.services.AutomationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("automationController")
@RequestMapping("/automation/v0")
@AllArgsConstructor
public class AutomationController {

    AutomationService automationService;

    @PostMapping("/alarm/arm")
    ResponseEntity<AlarmArmResponse> alarmArm (@RequestBody AlarmArmRequest alarmArmRequest) {
        log.info("------INIT ALARM ARM PROCESS------");
        AlarmArmResponse alarmArmResponse = automationService.alarmArm(alarmArmRequest);
        log.info("------FINISH PROCESS ALARM STATUS= {} ------", alarmArmResponse.getStatus().getStatus());
        return ResponseEntity.status(HttpStatus.OK.value()).body(alarmArmResponse);
    }

    @PostMapping("/alarm/disarm")
    ResponseEntity<AlarmDisarmResponse> alarmDisarm (@RequestBody AlarmDisarmRequest alarmDisarmRequest) {
        log.info("------INIT ALARM DISARM PROCESS------");
        AlarmDisarmResponse alarmDisarmResponse = automationService.alarmDisarm(alarmDisarmRequest);
        log.info("------FINISH PROCESS ALARM STATUS= {} ------", alarmDisarmResponse.getStatus().getStatus());
        return ResponseEntity.status(HttpStatus.OK.value()).body(alarmDisarmResponse);
    }

/*    @GetMapping("/led/toggle")
    ResponseEntity<ToggleLedResponse> toggleLed () {
        log.info("------INIT AUTOMATION_SERVICE------");
        ToggleLedResponse toggleLedResponse = automationService.toggleLed();
        log.info("------FINISH AUTOMATION_SERVICE------");
        return ResponseEntity.status(HttpStatus.OK.value()).body(toggleLedResponse);
    }

    @PostMapping("/strip/color")
    ResponseEntity<?> colorStripLed (@RequestBody LedStripRequest ledStripRequest) {
        log.info("------INIT AUTOMATION_SERVICE LEDSTRIP------");
        automationService.colorStripLed(ledStripRequest);
        log.info("------FINISH AUTOMATION_SERVICE LEDSTRIP------");
        return ResponseEntity.status(HttpStatus.OK.value()).build();
    }

    @GetMapping("/servo/{servo-percentage}")
    ResponseEntity<?> moveServo (@PathVariable("servo-percentage") Integer servoPercentage) {
        log.info("------INIT AUTOMATION_SERVICE moveServo------");
        automationService.moveServo(servoPercentage);
        log.info("------FINISH AUTOMATION_SERVICE moveServo------");
        return ResponseEntity.status(HttpStatus.OK.value()).build();
    }*/
}
