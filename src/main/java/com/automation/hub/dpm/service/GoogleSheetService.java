package com.automation.hub.dpm.service;

import com.automation.hub.client.GoogleSheetsClient;

import com.automation.hub.client.GoogleSheetsClient;
import com.automation.hub.dpm.model.DeviceMapping;
import com.automation.hub.service.DpmAutomationService;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class GoogleSheetService {

	 private Sheets sheets;

	    private Sheets getSheets() throws Exception {
	        if (sheets == null) {
	            sheets = GoogleSheetsClient.getSheetsService();
	        }
	        return sheets;
	    }

	    public List<String> groupDpmData(String sheetId, String gid) throws Exception {

	        GoogleSheetsClient.reset();

	        Spreadsheet spreadsheet = getSheets()
	                .spreadsheets()
	                .get(sheetId)
	                .setIncludeGridData(false)
	                .execute();

	        String selectedTab = null;

	        // If gid provided, match exact sheet
	        if (gid != null) {
	            for (var sheet : spreadsheet.getSheets()) {
	                if (sheet.getProperties().getSheetId().toString().equals(gid)) {
	                    selectedTab = sheet.getProperties().getTitle();
	                    System.out.println("🎯 Active tab detected via GID: " + selectedTab);
	                    break;
	                }
	            }
	        }

	        // Fallback if gid did not match
	        if (selectedTab == null) {
	            selectedTab = spreadsheet.getSheets().get(0).getProperties().getTitle();
	            System.out.println("⚠ No GID match. Using first tab: " + selectedTab);
	        }

	        String range = selectedTab + "!A3:AF";

	        ValueRange response = getSheets().spreadsheets().values()
	                .get(sheetId, range)
	                .execute();

	        List<List<Object>> rows = response.getValues();
	        Map<String, Set<String>> map = new LinkedHashMap<>();

	        if (rows != null) {
	            for (List<Object> row : rows) {

	                String dpm = row.size() > 31 ? row.get(31).toString().trim() : "";
	                String deviceId = row.size() > 10 ? row.get(10).toString().trim() : "";

	                if (!dpm.isEmpty() && !deviceId.isEmpty()) {
	                    map.computeIfAbsent(dpm, k -> new LinkedHashSet<>()).add(deviceId);
	                }
	            }
	        }

	        List<String> output = new ArrayList<>();
	        map.forEach((dpm, devices) ->
	                output.add("\"" + dpm + "\" - [" + String.join(", ", devices) + "]")
	        );

	        return output;
	    }

}
