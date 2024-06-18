package com.spring.iot.services;

import com.spring.iot.entities.Notification;
import com.spring.iot.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    NotificationRepository notificationRepository;
    public Integer getNumberOfUnreadNotification(){
        return notificationRepository.getUnReadNotification().size();
    }

    public List<Notification> getAllNotification(){
        return notificationRepository.findAllByOrderByIsReadAscTimeAsc();
    }
    public void readAllNotification(){
        List<Notification> l = notificationRepository.findAll();
        for (Notification n : l){
            n.setRead(true);
        }
        notificationRepository.saveAll(l);
    }

    public List<Notification> getAll(LocalDateTime fromDate, LocalDateTime toDate, String searchString) {
        // Call your repository method with the provided parameters
        return notificationRepository.findAllByContentContainingAndTimestampBetweenOrderByIsReadAscTimestampAsc(searchString, fromDate, toDate);
    }
}
