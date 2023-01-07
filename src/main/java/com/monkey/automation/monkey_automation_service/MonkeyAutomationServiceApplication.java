package com.monkey.automation.monkey_automation_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MonkeyAutomationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonkeyAutomationServiceApplication.class, args);
    }
}
