package com.automation.hub.dpm.service;

import com.automation.hub.dpm.model.DeviceMapping;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class GoogleSheetService {
	  @Value("${google.sheet.id}")
	    private String sheetId;

	    private final Sheets sheets;

	    public GoogleSheetService(@Qualifier("dpmSheetsClient") Sheets sheets) {
	        this.sheets = sheets;
	    }

	    public List<String> groupDpmData() throws IOException {

	        String range = "Sheet1!A3:AF";
	        ValueRange response = sheets.spreadsheets().values().get(sheetId, range).execute();
	        List<List<Object>> rows = response.getValues();

	        Map<String, Set<String>> map = new LinkedHashMap<>();

	        for (List<Object> row : rows) {

	            String dpm = row.size() > 31 ? row.get(31).toString().trim() : "";
	            String deviceId = row.size() > 10 ? row.get(10).toString().trim() : "";

	            if (!dpm.isEmpty() && !deviceId.isEmpty()) {
	                map.computeIfAbsent(dpm, k -> new LinkedHashSet<>()).add(deviceId);
	            }
	        }

	        // Format into the required string output
	        List<String> formattedOutput = new ArrayList<>();

	        map.forEach((dpm, devices) -> {
	            String deviceList = String.join(", ", devices);
	            formattedOutput.add("\"" + dpm + "\" - [" + deviceList + "]");
	        });

	        return formattedOutput;
	    }
}
