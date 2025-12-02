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
	    public List<String> getGroupedDetails(@RequestParam("sheet") String sheetUrl) throws Exception {

	        String sheetId = extractSheetId(sheetUrl);
	        System.out.println("✔ Using Sheet ID: " + sheetId);

	        return service.groupDpmData(sheetId);
	    }

	    private String extractSheetId(String url) {

	        // Case 1: Normal google sheet link `/d/{id}/edit`
	        if (url.contains("/d/")) {
	            int start = url.indexOf("/d/") + 3;
	            int end = url.indexOf("/", start);
	            return url.substring(start, end);
	        }

	        // Case 2: ?id=
	        if (url.contains("id=")) {
	            return url.substring(url.indexOf("id=") + 3);
	        }

	        // Case 3: Already ID
	        return url;
	    }
}
