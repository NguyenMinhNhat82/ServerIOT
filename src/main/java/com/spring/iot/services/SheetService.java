package com.spring.iot.services;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.spring.iot.entities.Station;
import com.spring.iot.repositories.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.*;

@Service
public class SheetService {


    @Autowired
    private StationRepository stationRepository;
    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens/path";

    /**
     * Global instance of the scopes required by this quickstart. If modifying these
     * scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS, SheetsScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = SheetService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(
                        new java.io.File(System.getProperty("user.home"), TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline").build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public Map<Object, Object> getDataFromSheet() throws GeneralSecurityException, IOException {
        // Build a new authorized API client service.
        final String spreadsheetId = "1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms";
        final String range = "Class Data!A2:E";
        Sheets service = getSheetService();
        ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
        List<List<Object>> values = response.getValues();
        Map<Object, Object> storeDataFromGoogleSheet = new HashMap<>();
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            for (List row : values) {
                storeDataFromGoogleSheet.put(row.get(0), row.get(4));
            }
        }
        return storeDataFromGoogleSheet;
    }

    private Sheets getSheetService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new Sheets.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
                .setApplicationName(APPLICATION_NAME).build();
    }

    private Drive getDriveService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new Drive.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
                .setApplicationName(APPLICATION_NAME).build();
    }

    public Boolean updateGoogleSheet()
            throws GeneralSecurityException, IOException {
        String idSheet = "1mvJZiq-LscDEXL4hvo4IJNHHHXNhNXeD0KD9Y56Rmu4";
        Sheets service = getSheetService();
        try {
            //Station
            List<List<Object>> listStation = new ArrayList<>();
            for (Station s : stationRepository.findAll()){
                List<Object> objects = Arrays.asList(s.getId(),s.getName(),String.valueOf(s.getActive()));
                listStation.add(objects);
            }
            ValueRange valueRangeStation = new ValueRange().setValues(listStation);
            service.spreadsheets().values().update(idSheet, "A4", valueRangeStation)
                    .setValueInputOption("RAW").execute();
            //Value
            List<List<Object>> listValue = new ArrayList<>();
            for (List<Object> s : stationRepository.getListValue()){
                List<Object> objects = Arrays.asList(s.get(0),s.get(1),s.get(2),s.get(3),String.valueOf(s.get(4)));
                listValue.add(objects);
            }
            ValueRange valueRangeStationValue = new ValueRange().setValues(listValue);
            service.spreadsheets().values().update(idSheet, "E4", valueRangeStationValue)
                    .setValueInputOption("RAW").execute();
            return true;
        }
        catch (Exception ex){
            return false;
        }

    }
}
