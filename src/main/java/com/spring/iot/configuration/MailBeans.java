package com.spring.iot.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;
@Configuration
public class MailBeans {
    @Bean
    public JavaMailSenderImpl configureJavaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setUsername("nhatnguyenn0802@gmail.com");
        javaMailSender.setPassword("axjiolgzsmfbxbju");
        javaMailSender.setPort(587);
        javaMailSender.setDefaultEncoding("UTF-8");
        Properties mailProp = new Properties();
        mailProp.put("mail.smtp.starttls.enable", true);
        mailProp.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        javaMailSender.setJavaMailProperties(mailProp);
        return javaMailSender;
    }
}
