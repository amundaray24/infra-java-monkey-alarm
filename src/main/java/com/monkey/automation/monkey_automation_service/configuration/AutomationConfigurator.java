package com.monkey.automation.monkey_automation_service.configuration;

import com.monkey.automation.monkey_automation_service.configuration.entities.StatusControllers;
import com.monkey.automation.monkey_automation_service.configuration.helpers.Buzzer;
import com.monkey.automation.monkey_automation_service.configuration.helpers.LcdDisplay;
import com.monkey.automation.monkey_automation_service.configuration.helpers.SimpleDigital;
import com.monkey.automation.monkey_automation_service.configuration.helpers.enums.PIN;
import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.PullResistance;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

@Configuration
@Slf4j
@AllArgsConstructor
public class AutomationConfigurator {

    @Bean
    @Scope(SCOPE_SINGLETON)
    public Map<StatusControllers,Boolean> dispatchController() {
        return new HashMap<>() {{
            put(StatusControllers.ARMED_CONTROL, Boolean.FALSE);
            put(StatusControllers.DISPATCHED_CONTROL, Boolean.FALSE);
        }};
    }

    @Bean
    @Scope(SCOPE_SINGLETON)
    public Context pi4jAutoContext() {
        return Pi4J.newAutoContext();
    }

    @Bean(name = "buzzerAlarm")
    @Scope(SCOPE_SINGLETON)
    public SimpleDigital buzzerAlarm (Context pi4jAutoContext){
        return new SimpleDigital(pi4jAutoContext, PIN.D4,DigitalState.HIGH);
    }

    @Bean(name = "checkAlarmButton")
    @Scope(SCOPE_SINGLETON)
    public DigitalInput checkAlarmButton (Context pi4jAutoContext){
        return pi4jAutoContext.create(DigitalInput.newConfigBuilder(pi4jAutoContext)
                .id("BCM" + 27)
                .name("Button #" + 27)
                .address(27)
                .debounce(3000L)
                .pull(PullResistance.PULL_DOWN)
                .provider("pigpio-digital-input")
                .build());
    }

    @Bean(name = "movementSensor")
    @Scope(SCOPE_SINGLETON)
    public DigitalInput movementSensor (Context pi4jAutoContext){
        return pi4jAutoContext.create(DigitalInput.newConfigBuilder(pi4jAutoContext)
                .id("BCM" + 22)
                .name("Button #" + 22)
                .address(22)
                .debounce(3000L)
                .pull(PullResistance.PULL_DOWN)
                .provider("pigpio-digital-input")
                .build());
    }

    @Bean(name = "lcdDisplay")
    public LcdDisplay lcdDisplay (Context pi4jAutoContext){
        return new LcdDisplay(pi4jAutoContext, 2,16);
    }

/*    @Bean
    @Scope(SCOPE_SINGLETON)
    public SimpleDigital simpleLed (Context pi4jAutoContext){
        return new SimpleDigital(pi4jAutoContext, PIN.D22);
    }

    @Bean
    @Scope(SCOPE_SINGLETON)
    public LedStrip ledStrip (Context pi4jAutoContext){
        return new LedStrip(pi4jAutoContext, 8, 0.1);
    }

    @Bean
    @Scope(SCOPE_SINGLETON)
    public ServoMotor servoMotor (Context pi4jAutoContext){
        return new ServoMotor(pi4jAutoContext, PIN.PWM18);
    }*/


}
