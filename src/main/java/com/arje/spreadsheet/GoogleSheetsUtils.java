package com.arje.spreadsheet;

import com.arje.helpers.SimpleRow;
import com.arje.helpers.SimpleSheet;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

public class GoogleSheetsUtils {
    private static final String APPLICATION_NAME = "Google Sheets Example";

    public static Iterator<SimpleSheet> getSheetsFromGoogleSpreadsheet(String spreadsheetUrl) throws GeneralSecurityException, IOException {

        String spreadsheetId = getSpreadsheetIdFromUrl(spreadsheetUrl);
        Sheets sheetsService = getSheetsService();
        List<String> ranges = getSheetTitlesFromSpreadsheet(spreadsheetId, sheetsService);

        List<SimpleSheet> sheets = new ArrayList<>();
        BatchGetValuesResponse result = sheetsService.spreadsheets().values()
                .batchGet(spreadsheetId)
                .setRanges(ranges).execute();

        for (ValueRange valueRange : result.getValueRanges()) {
            SimpleSheet sheet = new SimpleSheet();
            for (List<Object> value : valueRange.getValues()) {
                SimpleRow row = new SimpleRow();
                for (Object cell : value) {
                    row.add(String.valueOf(cell));
                }
                if (row.getCells().isEmpty()) {
                    row.add("");
                }
                sheet.add(row);
            }
            sheets.add(sheet);
        }
        return sheets.iterator();
    }

    private static String getSpreadsheetIdFromUrl(String spreadsheetUrl) {
        return spreadsheetUrl
                .split("/d/")[1]
                .split("/")[0];
    }

    private static List<String> getSheetTitlesFromSpreadsheet(String spreadsheetId, Sheets sheetsService) throws IOException {
        Spreadsheet execute = sheetsService
                .spreadsheets()
                .get(spreadsheetId)
                .execute();

        List<String> ranges = new ArrayList<>();
        for (Sheet sheet : execute.getSheets()) {
            String title = ((SheetProperties) sheet.get("properties")).getTitle();
            ranges.add("'" + title + "'");
        }
        return ranges;
    }

    private static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(), getCredentials())
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private static HttpRequestInitializer getCredentials() throws IOException {
        return GoogleCredential
                .fromStream((Objects.requireNonNull(GoogleSheetsUtils.class.getResourceAsStream("/google-service-account-credentials.json"))))
                .createScoped(SheetsScopes.all());
    }
}
