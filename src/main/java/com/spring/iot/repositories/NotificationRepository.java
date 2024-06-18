package com.spring.iot.repositories;

import com.spring.iot.entities.Notification;
import com.spring.iot.entities.Sensor;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    @Query("select n from Notification n where n.isRead = false ")
    public List<Notification> getUnReadNotification();

    List<Notification> findAllByOrderByIsReadAscTimeAsc();


    @Query("SELECT sd FROM Notification sd " +
            "WHERE (:searchString IS NULL OR sd.content LIKE %:searchString%) " +
             "and(sd.time between :toDate and :fromDate) " +
            "ORDER BY sd.isRead ASC, sd.time ASC")
    List<Notification> findAllByContentContainingAndTimestampBetweenOrderByIsReadAscTimestampAsc(
            @Param("searchString") String searchString,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate);

    @Query("select n from Notification  n where n.time <= :toDate")
    List<Notification> findByToDate(@Param("toDate") LocalDateTime toDate);
    @Query("select n from Notification  n where n.time <= :fromDate")
    List<Notification> findByFromDate(@Param("fromDate") LocalDateTime fromDate);
    @Query("select n from Notification  n where n.time <= :toDate and n.time >= :fromDate")
    List<Notification> findByToDateDateAndFromDate(@Param("toDate") LocalDateTime toDate,@Param("fromDate") LocalDateTime fromDate);

}
