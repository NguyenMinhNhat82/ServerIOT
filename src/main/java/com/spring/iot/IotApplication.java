package com.spring.iot;

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

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        SpringApplication.run(IotApplication.class, args);

    }

    @Scheduled(fixedRate = 120000)
    public synchronized void keepServerAlive() {
        RestTemplate restTemplate = new RestTemplate();
        String url =  "http://localhost:9000/test";
        ResponseEntity<String> response
                = restTemplate.getForEntity(url , String.class);
    }

}
