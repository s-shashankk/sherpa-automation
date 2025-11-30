package com.automation.hub.controller;

import com.automation.hub.service.DpmAutomationService;
import com.automation.hub.web.SheetRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AutomationController {
	 private final DpmAutomationService service;

	    public AutomationController(DpmAutomationService service) {
	        this.service = service;
	    }

	    @PostMapping("/run")
	    public String runAutomation(@RequestBody SheetRequest req) {
	        try {
	            return service.runAutomation(req.getValidationUrl(), req.getWorkbookUrl());
	        } catch (Exception e) {
	            return "❌ ERROR: " + e.getMessage();
	        }
	    }
}
