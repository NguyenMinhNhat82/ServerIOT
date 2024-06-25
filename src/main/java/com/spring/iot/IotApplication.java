package com.spring.iot;

import com.spring.iot.dto.Status;
import com.spring.iot.entities.SensorValue;
import com.spring.iot.repositories.SensorRepository;
import com.spring.iot.repositories.SensorValueRepository;
import com.spring.iot.services.SensorService;
import com.spring.iot.services.SheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


@SpringBootApplication
@EnableScheduling
public class IotApplication {
    @Autowired
    SheetService sheetService;

    @Autowired
    SensorService sensorService;

    @Autowired
    SensorValueRepository sensorValueRepository;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        SpringApplication.run(IotApplication.class, args);

    }

    @Scheduled(fixedRate = 1000*60*2)
    public synchronized void keepServerAlive() throws GeneralSecurityException, IOException {
        RestTemplate restTemplate = new RestTemplate();
        System.out.println("reset");
        String url =  "https://iotcontroller-1.onrender.com/test";
        //http://localhost:9000
//        https://serveriot-ob37.onrender.com/

        //https://serveriot-1.onrender.com/test
        ResponseEntity<String> response
                = restTemplate.getForEntity(url , String.class);
    }

//    @Scheduled(fixedRate = 1000*60*2)
//    public synchronized void autoGenerateNumber() throws GeneralSecurityException, IOException {
//        ZoneId zid = ZoneId.of("Asia/Saigon");
//        Random random = new Random();
//        DecimalFormat df = new DecimalFormat("#.##");
//
//        SensorValue sensorValue1 = new SensorValue(0,String.valueOf(df.format(random.nextFloat(3) + 20)),
//                LocalDateTime.now(zid),sensorService.findSensorByID("temp_0001"));
//        SensorValue sensorValue2 = new SensorValue(0,String.valueOf(df.format(random.nextFloat(5) + 60)),
//                LocalDateTime.now(zid),sensorService.findSensorByID("humi_0001"));
//        SensorValue sensorValue3 = new SensorValue(0,String.valueOf(df.format(random.nextFloat(4) + 20)),
//                LocalDateTime.now(zid),sensorService.findSensorByID("temp_0002"));
//        SensorValue sensorValue4 = new SensorValue(0,String.valueOf(df.format(random.nextFloat(6) + 60)),
//                LocalDateTime.now(zid),sensorService.findSensorByID("humi_0002"));
//        SensorValue sensorValue5 = new SensorValue(0,String.valueOf(df.format(random.nextFloat(7) + 4)),
//                LocalDateTime.now(zid),sensorService.findSensorByID("ph_0002"));
//        SensorValue sensorValue6 = new SensorValue(0,String.valueOf(df.format(random.nextFloat(3)+2)),
//                LocalDateTime.now(zid),sensorService.findSensorByID("EC_0002"));
//        SensorValue sensorValue7 = new SensorValue(0,String.valueOf(df.format(random.nextFloat(3)+4)),
//                LocalDateTime.now(zid),sensorService.findSensorByID("Nito_0002"));
//        SensorValue sensorValue8 = new SensorValue(0,String.valueOf(df.format(random.nextFloat(30)+20)),
//                LocalDateTime.now(zid),sensorService.findSensorByID("Photpho_0002"));
//        SensorValue sensorValue9 = new SensorValue(0,String.valueOf(df.format(random.nextFloat(15) + 5)),
//                LocalDateTime.now(zid),sensorService.findSensorByID("Kali_0002"));
//        SensorValue sensorValue10 = new SensorValue(0,"true",
//                LocalDateTime.now(zid),sensorService.findSensorByID("Relay_0001"));
//        SensorValue sensorValue11 = new SensorValue(0,"false",
//                LocalDateTime.now(zid),sensorService.findSensorByID("Relay_0002"));
//        SensorValue sensorValue12 = new SensorValue(0,"false",
//                LocalDateTime.now(zid),sensorService.findSensorByID("Relay_0003"));
//        SensorValue sensorValue13 = new SensorValue(0,"true",
//                LocalDateTime.now(zid),sensorService.findSensorByID("Relay_0004"));
//        sensorValueRepository.save(sensorValue1);
//        sensorValueRepository.save(sensorValue2);
//        sensorValueRepository.save(sensorValue3);
//        sensorValueRepository.save(sensorValue4);
//        sensorValueRepository.save(sensorValue5);
//        sensorValueRepository.save(sensorValue6);
//        sensorValueRepository.save(sensorValue7);
//        sensorValueRepository.save(sensorValue8);
//        sensorValueRepository.save(sensorValue9);
//        sensorValueRepository.save(sensorValue10);
//        sensorValueRepository.save(sensorValue11);
//        sensorValueRepository.save(sensorValue12);
//        sensorValueRepository.save(sensorValue13);
//        String jsonString = "{\"station_id\":\"air_0002\",\"station_name\":\"NBIOT 0002\",\"sensors\":[{\"id\":\"temp_0001\",\"value\":" + String.valueOf(df.format(random.nextFloat(3) + 20)) +"}" +
//                ",{\"id\":\"humi_0001\",\"value\":"+ String.valueOf(df.format(random.nextFloat(5) + 60))+"}," +
//                "{\"id\":\"temp_0002\",\"value\":" + String.valueOf(df.format(random.nextFloat(4) + 20))+"}," +
//                "{\"id\":\"humi_0002\",\"value\":"+String.valueOf(df.format(random.nextFloat(6) + 60))+"}," +
//                "{\"id\":\"ph_0002\",\"value\":"+String.valueOf(df.format(random.nextFloat(7) + 4))+"}," +
//                "{\"id\":\"EC_0002\",\"value\":"+String.valueOf(df.format(random.nextFloat(3)+2))+"}," +
//                "{\"id\":\"Nito_0002\",\"value\":"+String.valueOf(df.format(random.nextFloat(3)+4))+"}," +
//                "{\"id\":\"Photpho_0002\",\"value\":"+String.valueOf(df.format(random.nextFloat(30)+20))+"}," +
//                "{\"id\":\"Kali_0002\",\"value\":"+String.valueOf(df.format(random.nextFloat(15) + 5))+"},{\"id\":\"Relay_0001\",\"value\":true},{\"id\":\"Relay_0002\",\"value\":false},{\"id\":\"Relay_0003\",\"value\":false},{\"id\":\"Relay_0004\",\"value\":false}]}";
//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        Calendar cal = Calendar.getInstance();
//        com.spring.iot.dto.Message m = new com.spring.iot.dto.Message("server", "client", jsonString, dateFormat.format(cal.getTime()), Status.MESSAGE);
//        simpMessagingTemplate.convertAndSendToUser(m.getReceiverName(), "/private", m);
//        System.out.println("Success");
//    }


}
