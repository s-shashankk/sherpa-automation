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

	 private Sheets sheets;

	    // ⭐ Lazy load Sheets client (works local + cloud)
	    private Sheets getSheets() throws Exception {
	        if (sheets == null) {
	            sheets = GoogleSheetsClient.getSheetsService();
	        }
	        return sheets;
	    }

	    /**
	     * Build mapping from DPM → [deviceId1, deviceId2, ...]
	     * using a dynamic sheetId passed from the controller.
	     */
	    public List<String> groupDpmData(String sheetId) throws Exception {

	        // Make sure we don't reuse an old cached client
	        GoogleSheetsClient.reset();

	        String range = "Sheet1!A3:AF";

	        ValueRange response = getSheets()
	                .spreadsheets()
	                .values()
	                .get(sheetId, range)
	                .execute();

	        List<List<Object>> rows = response.getValues();

	        Map<String, Set<String>> map = new LinkedHashMap<>();

	        if (rows != null) {
	            for (List<Object> row : rows) {

	                // AF column index = 31 (0-based)
	                String dpm = row.size() > 31 ? row.get(31).toString().trim() : "";
	                // Kunal gave: deviceId at column K (index 10)
	                String deviceId = row.size() > 10 ? row.get(10).toString().trim() : "";

	                if (!dpm.isEmpty() && !deviceId.isEmpty()) {
	                    map.computeIfAbsent(dpm, k -> new LinkedHashSet<>()).add(deviceId);
	                }
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
