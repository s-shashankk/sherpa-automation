package com.automation.hub.dpm.service;

import com.automation.hub.client.GoogleSheetsClient;

import com.automation.hub.client.GoogleSheetsClient;
import com.automation.hub.dpm.model.DeviceMapping;
import com.automation.hub.service.DpmAutomationService;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class GoogleSheetService {

    private final DpmAutomationService dpmAutomationService;
	 @Value("${google.sheet.id}")
	    private String sheetId;

	    private Sheets sheets;

    GoogleSheetService(DpmAutomationService dpmAutomationService) {
        this.dpmAutomationService = dpmAutomationService;
    }

	    // ⭐ Lazy load Sheets client (works local + cloud)
	    private Sheets getSheets() throws Exception {
	        if (sheets == null) {
	            sheets = GoogleSheetsClient.getSheetsService();
	        }
	        return sheets;
	    }

	    public List<String> groupDpmData() throws Exception {

	        String range = "Sheet1!A3:AF";
	        ValueRange response = getSheets().spreadsheets().values().get(sheetId, range).execute();
	        List<List<Object>> rows = response.getValues();

	        Map<String, Set<String>> map = new LinkedHashMap<>();

	        for (List<Object> row : rows) {

	            String dpm = row.size() > 31 ? row.get(31).toString().trim() : "";
	            String deviceId = row.size() > 10 ? row.get(10).toString().trim() : "";

	            if (!dpm.isEmpty() && !deviceId.isEmpty()) {
	                map.computeIfAbsent(dpm, k -> new LinkedHashSet<>()).add(deviceId);
	            }
	        }

	        List<String> formattedOutput = new ArrayList<>();

	        map.forEach((dpm, devices) -> {
	            String deviceList = String.join(", ", devices);
	            formattedOutput.add("\"" + dpm + "\" - [" + deviceList + "]");
	        });

	        return formattedOutput;
	    }
}
