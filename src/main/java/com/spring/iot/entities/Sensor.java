package com.spring.iot.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sensor")
@Data
public class Sensor {
    @Id
    private String id;
    private Boolean active;
    private Boolean isSchedule;
    private LocalDateTime timeSchedule;



    @ManyToOne
    @JoinColumn(name = "id_station")
    private Station station;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sensor",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<SensorValue> sensorValues = new HashSet<>();


    private Integer taskID ;



    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getSchedule() {
        return isSchedule;
    }

    public void setSchedule(Boolean schedule) {
        isSchedule = schedule;
    }

    public LocalDateTime getTimeSchedule() {
        return timeSchedule;
    }

    public void setTimeSchedule(LocalDateTime timeSchedule) {
        this.timeSchedule = timeSchedule;
    }

    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public Sensor() {
    }

    public Sensor(String id, Station station) {
        this.id = id;
        this.station = station;
    }
}