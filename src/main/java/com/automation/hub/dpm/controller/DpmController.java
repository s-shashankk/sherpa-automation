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
	        String gid      = extractGid(sheetUrl);

	        System.out.println("✔ Sheet ID: " + sheetId);
	        System.out.println("✔ Active Sheet GID: " + gid);

	        return service.groupDpmData(sheetId, gid);
	    }

	    /** Extract sheet ID from Google Sheets URL */
	    private String extractSheetId(String url) {
	        if (url.contains("/d/")) {
	            int start = url.indexOf("/d/") + 3;
	            int end = url.indexOf("/", start);
	            return url.substring(start, end);
	        }
	        if (url.contains("id=")) {
	            return url.substring(url.indexOf("id=") + 3);
	        }
	        return url;
	    }

	    /** Extract the GID (sheet tab identifier) */
	    private String extractGid(String url) {
	        if (url.contains("gid=")) {
	            return url.substring(url.indexOf("gid=") + 4).trim();
	        }
	        return null; // fallback if not present
	    }
}
