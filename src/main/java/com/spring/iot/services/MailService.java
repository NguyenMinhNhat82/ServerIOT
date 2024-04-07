package com.spring.iot.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Autowired
    private JavaMailSenderImpl javaMailSender;

    public void sendMail(String username,String email,int otpCode) {
        SimpleMailMessage newEmail = new SimpleMailMessage();
        newEmail.setTo(email);
        newEmail.setSubject("Xác thực OTP");
        newEmail.setText("Xin chào " + username +"\nMã OTP của bạn để đăng kí tài khoản là " +  otpCode);
        javaMailSender.send(newEmail);
    }

}
