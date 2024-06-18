package com.spring.iot.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private String content;
    private String state;
    private String value;
    private LocalDateTime time;
    private Boolean isRead;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Notification(Integer id, String content, String state, String value, LocalDateTime time, Boolean isRead) {
        this.id = id;
        this.content = content;
        this.state = state;
        this.value = value;
        this.time = time;
        this.isRead = isRead;
    }

    public Notification() {
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }
}
