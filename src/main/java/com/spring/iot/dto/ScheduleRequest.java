package com.spring.iot.dto;


public class ScheduleRequest {
    private String idSensor;
    private String time;

    public ScheduleRequest(String idSensor, String time) {
        this.idSensor = idSensor;
        this.time = time;
    }

    public ScheduleRequest() {
    }

    public String getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(String idSensor) {
        this.idSensor = idSensor;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
