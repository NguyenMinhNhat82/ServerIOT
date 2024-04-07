package com.spring.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.security.GeneralSecurityException;


@SpringBootApplication
public class IotApplication {

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        SpringApplication.run(IotApplication.class, args);

    }

}
