package com.automation.hub.dpm.controller;
import com.automation.hub.dpm.model.DeviceMapping;
import com.automation.hub.dpm.service.GoogleSheetService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping("/dpm")
public class DpmController {
	  private final GoogleSheetService service;

	    public DpmController(GoogleSheetService service) {
	        this.service = service;
	    }

	    @GetMapping("/group")
	    public List<String> getGroupedDetails() throws Exception {
	        return service.groupDpmData();
	    }
}
