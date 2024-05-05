package com.spring.iot;

import com.spring.iot.services.SheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;


@SpringBootApplication
@EnableScheduling
public class IotApplication {
    @Autowired
    SheetService sheetService;

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        SpringApplication.run(IotApplication.class, args);

    }

    @Scheduled(fixedRate = 1200000)
    public synchronized void keepServerAlive() throws GeneralSecurityException, IOException {
        sheetService.update();
//        RestTemplate restTemplate = new RestTemplate();
//        String url =  "https://serveriot-0z1m.onrender.com/test";
//        //http://localhost:9000
//
//        //https://serveriot-1.onrender.com/test
//        ResponseEntity<String> response
//                = restTemplate.getForEntity(url , String.class);
    }

}
