package com.automation.hub.client;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.GridData;
import com.google.api.services.sheets.v4.model.RowData;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GoogleSheetsClient {
	
	 private static Sheets sheetsService;

	    public static Sheets getSheetsService() throws Exception {

	        if (sheetsService == null) {

	            String credentialsJson = System.getenv("GOOGLE_APPLICATION_CREDENTIALS_JSON");
	            InputStream stream;

	            if (credentialsJson != null && !credentialsJson.isEmpty()) {
	                System.out.println("🔹 Using ENV credentials");
	                stream = new ByteArrayInputStream(credentialsJson.getBytes(StandardCharsets.UTF_8));
	            } else {
	                System.out.println("🔹 Using LOCAL file credentials");
	                stream = new FileInputStream("src/main/resources/credentials.json");
	            }

	            GoogleCredentials googleCredentials = GoogleCredentials
	                    .fromStream(stream)
	                    .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));

	            sheetsService = new Sheets.Builder(
	                    GoogleNetHttpTransport.newTrustedTransport(),
	                    GsonFactory.getDefaultInstance(),
	                    new HttpCredentialsAdapter(googleCredentials)
	            )
	            .setApplicationName("Automation Hub")
	            .build();
	        }
	        return sheetsService;
	    }

	    /** ⭐ RESET before every new automation run */
	    public static void reset() {
	        System.out.println("♻ Resetting cached Google Sheets Client...");
	        sheetsService = null;
	    }

	    public static List<List<Object>> readSheet(String spreadsheetId, String range) throws Exception {
	        Sheets service = getSheetsService();
	        ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
	        return response.getValues();
	    }

	    /** ⭐ Read NOTES in a range (needed for multi-carrier mapping) */
	    public static List<String> readNotesForRange(String spreadsheetId, String rangeA1) throws Exception {

	        Sheets service = getSheetsService();

	        Spreadsheet spreadsheet = service.spreadsheets()
	                .get(spreadsheetId)
	                .setRanges(Collections.singletonList(rangeA1))
	                .setIncludeGridData(true)
	                .execute();

	        List<String> notes = new ArrayList<>();

	        for (Sheet sheet : spreadsheet.getSheets()) {
	            List<GridData> dataList = sheet.getData();
	            if (dataList == null) continue;

	            for (GridData gridData : dataList) {
	                List<RowData> rowDataList = gridData.getRowData();
	                if (rowDataList == null) continue;

	                for (RowData rowData : rowDataList) {
	                    List<CellData> cellDataList = rowData.getValues();
	                    if (cellDataList == null || cellDataList.isEmpty()) {
	                        notes.add("");
	                    } else {
	                        CellData cell = cellDataList.get(0);
	                        String note = cell.getNote();
	                        notes.add(note != null ? note : "");
	                    }
	                }
	            }
	        }

	        return notes;
	    }

	    public static void writeColumn(String spreadsheetId, String range, ValueRange valueRange) throws Exception {
	        Sheets service = getSheetsService();
	        service.spreadsheets().values()
	                .update(spreadsheetId, range, valueRange)
	                .setValueInputOption("USER_ENTERED")
	                .execute();
	    }
}

	/*
	 * private static Sheets getSheetsService() throws Exception { if (sheetsService
	 * == null) {
	 * 
	 * FileInputStream serviceAccountStream = new
	 * FileInputStream("src/main/resources/credentials.json");
	 * 
	 * GoogleCredentials googleCredentials =
	 * GoogleCredentials.fromStream(serviceAccountStream)
	 * .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
	 * 
	 * sheetsService = new
	 * Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
	 * GsonFactory.getDefaultInstance(), new
	 * HttpCredentialsAdapter(googleCredentials))
	 * .setApplicationName("Automation Hub").build(); } return sheetsService; }
	 */

	

