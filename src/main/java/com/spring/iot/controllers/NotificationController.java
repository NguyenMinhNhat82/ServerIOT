package com.spring.iot.controllers;

import com.spring.iot.entities.Notification;
import com.spring.iot.entities.User;
import com.spring.iot.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
public class NotificationController {
    @Autowired
    NotificationService notificationService;

    @GetMapping("/api/notification/get-all")
    public ResponseEntity<List<Notification>> getAllNotification(){
        return new ResponseEntity<>(notificationService.getAllNotification(), HttpStatus.OK);
    }
    @GetMapping("/api/notification/get-num-unread")
    public ResponseEntity<Integer> getUnreadNumber(){
        return new ResponseEntity<>(notificationService.getNumberOfUnreadNotification(), HttpStatus.OK);
    }
    @GetMapping("/api/notification/read-all")
    public void readAllNotification(){
        notificationService.readAllNotification();
    }

    @GetMapping("/api/notification/get0-all")
    public ResponseEntity<List<Notification>> getAllUser(@RequestParam Map<String, String> params) {
        String from = params.get("from");
        String to = params.get("to");
        String kw = params.get("kw");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Parse the string into LocalDateTime
        LocalDateTime dateTimeFrom = from == null? null: LocalDateTime.parse(from, formatter);
        LocalDateTime dateTimeTo = to == null? null: LocalDateTime.parse(to, formatter);

        return new ResponseEntity<>(notificationService.getAll(dateTimeFrom, dateTimeTo, kw== null?"":kw), HttpStatus.OK);
    }


}
