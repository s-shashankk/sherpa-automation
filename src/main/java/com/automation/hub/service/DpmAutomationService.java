package com.automation.hub.service;
import com.automation.hub.client.GoogleSheetsClient;
import com.automation.hub.logic.MappingEngine;
import com.automation.hub.logic.ValidationParser;
import com.automation.hub.logic.WorkbookReader;
import com.automation.hub.logic.WorkbookUpdater;
import com.automation.hub.model.ValidationEntry;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
@Service
public class DpmAutomationService {
	//private final String validationSheetId = "1PhVuBhkLYpBGmTeGOcfPC8ZCN0b-YP11Mw9cqdoEqaw";
  //  private final String workbookSheetId   = "1-1MuTCEzHCNEpZsOMNrD4Z9JVMZZMCc23r94GnC9Q1U";

    public String runAutomation(String validationUrl, String workbookUrl) throws Exception {

        String validationSheetId = extractSheetId(validationUrl);
        String workbookSheetId   = extractSheetId(workbookUrl);

        // 1) Readingggg validation sheet
        List<List<Object>> validationRows = GoogleSheetsClient.readSheet(validationSheetId, "B7:T");
        List<String> validationNotes      = GoogleSheetsClient.readNotesForRange(validationSheetId, "B7:B");

        // 2) Parseee entries
        List<ValidationEntry> parsedEntries = ValidationParser.parse(validationRows, validationNotes);

        // 3) Mapping
        Map<String, String> dpmMap = MappingEngine.buildDpmMap(parsedEntries);

        // 4) Read workbook
        List<List<Object>> workbookRows = GoogleSheetsClient.readSheet(workbookSheetId, "A3:AX");

        // 5) Build keysssss
        List<String> workbookKeys = WorkbookReader.buildWorkbookKeys(workbookRows);

        // 6) Generating update values
        ValueRange updateValues = WorkbookUpdater.generateDpmUpdates(workbookKeys, dpmMap);

        // 7) Write to sheet
        GoogleSheetsClient.writeColumn(workbookSheetId, "AF3:AF", updateValues);

        return "✔ DPM Automation Completed Successfully.\n"
             + "Validation rows: " + validationRows.size() + "\n"
             + "Mapped entries: " + dpmMap.size() + "\n"
             + "Workbook rows processed: " + workbookRows.size();
    }
    private String extractSheetId(String url) {
        return url.substring(url.indexOf("/d/") + 3, url.indexOf("/edit"));
    }


}
