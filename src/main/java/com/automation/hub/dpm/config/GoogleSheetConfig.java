package com.automation.hub.dpm.config;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.http.HttpCredentialsAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Configuration
public class GoogleSheetConfig {
	@Value("${google.sheet.credentials}")
    private String credentialsPath;

    @Bean(name = "dpmSheetsClient")
    public Sheets sheetsService() throws IOException, GeneralSecurityException {

        GoogleCredentials credentials = GoogleCredentials.fromStream(
                new FileInputStream(credentialsPath)
        ).createScoped(List.of("https://www.googleapis.com/auth/spreadsheets"));

        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName("DPM Feature Sheets Client")
         .build();
    }
}
