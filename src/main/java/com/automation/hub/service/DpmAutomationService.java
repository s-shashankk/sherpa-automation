package com.automation.hub.service;
import com.automation.hub.client.GoogleSheetsClient;
import com.automation.hub.logic.MappingEngine;
import com.automation.hub.logic.ValidationParser;
import com.automation.hub.logic.WorkbookReader;
import com.automation.hub.logic.WorkbookUpdater;
import com.automation.hub.model.ValidationEntry;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
@Service
public class DpmAutomationService {
	//private final String validationSheetId = "1PhVuBhkLYpBGmTeGOcfPC8ZCN0b-YP11Mw9cqdoEqaw";
  //  private final String workbookSheetId   = "1-1MuTCEzHCNEpZsOMNrD4Z9JVMZZMCc23r94GnC9Q1U";

	 public String runAutomation(String validationUrl, String workbookUrl) throws Exception {

	        String validationSheetId = extractSheetId(validationUrl);
	        String workbookSheetId   = extractSheetId(workbookUrl);

	        // ⛔ FIX — Clear cached sheet client so new link loads fresh data
	        GoogleSheetsClient.reset();

	        System.out.println("📄 Validation Sheet: " + validationSheetId);
	        System.out.println("📄 Workbook Sheet: " + workbookSheetId);

	        // 1️⃣ Read validation sheet
	        List<List<Object>> validationRows = GoogleSheetsClient.readSheet(validationSheetId, "B7:T");

	        // 2️⃣ Get notes column (for multiple carriers in one row)
	        List<String> validationNotes = GoogleSheetsClient.readNotesForRange(validationSheetId, "B7:B");

	        // 3️⃣ Parse into objects
	        List<ValidationEntry> parsedEntries = ValidationParser.parse(validationRows, validationNotes);

	        // 4️⃣ Create mapping table
	        Map<String, String> dpmMap = MappingEngine.buildDpmMap(parsedEntries);

	        // 5️⃣ Read workbook sheet values
	        List<List<Object>> workbookRows = GoogleSheetsClient.readSheet(workbookSheetId, "A3:AX");

	        // 6️⃣ Convert workbook rows to lookup keys
	        List<String> workbookKeys = WorkbookReader.buildWorkbookKeys(workbookRows);

	        // 7️⃣ Generate sheet Update values
	        ValueRange updateValues = WorkbookUpdater.generateDpmUpdates(workbookKeys, dpmMap);

	        // 8️⃣ Write back to AF column
	        GoogleSheetsClient.writeColumn(workbookSheetId, "AF3:AF", updateValues);

	        return "✔ DPM Mapping Completed Successfully!\n"
	                + "📌 Validation rows read: " + validationRows.size() + "\n"
	                + "📌 Mapping entries generated: " + dpmMap.size() + "\n"
	                + "📌 Workbook rows updated: " + workbookRows.size();
	    }

	    private String extractSheetId(String url) {
	    	 if (url == null || url.isBlank()) {
	    	        throw new IllegalArgumentException("❌ Sheet URL is empty");
	    	    }

	    	    // Case 1: Standard format → /d/<ID>/
	    	    if (url.contains("/d/")) {
	    	        int start = url.indexOf("/d/") + 3;

	    	        // find next slash after the ID
	    	        int end = url.indexOf("/", start);

	    	        if (end > start) {
	    	            return url.substring(start, end);
	    	        }
	    	    }

	    	    // Case 2: Drive share format → ?id=<ID>
	    	    if (url.contains("id=")) {
	    	        return url.substring(url.indexOf("id=") + 3).trim();
	    	    }

	    	    // Case 3: User pasted only sheet ID already
	    	    return url.trim();
	    }



}
