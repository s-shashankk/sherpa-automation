package com.automation.hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.automation.hub.service.DpmAutomationService;

@SpringBootApplication
public class AutomationHubApplication {

	public static void main(String[] args) {
        SpringApplication.run(AutomationHubApplication.class, args);
    }
}
