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
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import com.spring.iot.entities.SensorValue;
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

    @Autowired
    private SensorValueService sensorService;

    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens/path";
    private static final List<String> SCOPES =
            Arrays.asList(SheetsScopes.SPREADSHEETS, SheetsScopes.DRIVE, SheetsScopes.DRIVE_FILE);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
        InputStream in = SheetService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private static Sheets getSheetService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new Sheets.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
                .setApplicationName(APPLICATION_NAME).build();
    }
    public  void update() throws IOException, GeneralSecurityException {
        final String spreadsheetId = "1mvJZiq-LscDEXL4hvo4IJNHHHXNhNXeD0KD9Y56Rmu4";
        final String range = "A3:C4";
        Sheets service = getSheetService();
        List<List<Object>> valueStation = new ArrayList<>();
        for(Station station : stationRepository.findAll()){
            List<Object> list = Arrays.asList(station.getId(), station.getName(), station.getActive().toString());
            valueStation.add(list);
        }

        List<List<Object>> valueSensor = new ArrayList<>();
        for(SensorValue sensorValue: sensorService.DataAllSensorInDay()){
            List<Object> list = Arrays.asList(sensorValue.getId(), sensorValue.getSensor().getStation().getName()
                    , sensorValue.getSensor().getId(), sensorValue.getValue(), sensorValue.getTimeUpdate().toString());
            valueSensor.add(list);
        }

        ValueRange valueRangeStation = new ValueRange().setValues(valueStation);
        ValueRange valueRangeSensorValue = new ValueRange().setValues(valueSensor);
        service.spreadsheets().values().update(spreadsheetId, "A4", valueRangeStation)
                .setValueInputOption("RAW").execute();

        service.spreadsheets().values().update(spreadsheetId, "E4", valueRangeSensorValue)
                .setValueInputOption("RAW").execute();
    }
}
