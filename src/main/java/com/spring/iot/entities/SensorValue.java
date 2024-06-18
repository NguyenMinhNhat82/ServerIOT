package com.spring.iot.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_value")
@Data
public class SensorValue {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private String value;

    @Column(name = "time_update")
    private LocalDateTime timeUpdate;

    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LocalDateTime getTimeUpdate() {
        return timeUpdate;
    }

    public void setTimeUpdate(LocalDateTime timeUpdate) {
        this.timeUpdate = timeUpdate;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public SensorValue(Integer id, String value, LocalDateTime timeUpdate, Sensor sensor) {
        this.id = id;
        this.value = value;
        this.timeUpdate = timeUpdate;
        this.sensor = sensor;
    }

    public SensorValue() {
    }
}