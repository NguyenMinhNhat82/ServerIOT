package com.spring.iot.dto;

import com.spring.iot.entities.SensorValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


public class HistoryDataResponse {
    List<SensorValue> values1h;
    List<SensorValue> values1d;
    List<SensorValue> values1w;
    List<SensorValue> values1m;

    public HistoryDataResponse(List<SensorValue> values1h, List<SensorValue> values1d, List<SensorValue> values1w, List<SensorValue> values1m) {
        this.values1h = values1h;
        this.values1d = values1d;
        this.values1w = values1w;
        this.values1m = values1m;
    }

    public List<SensorValue> getValues1h() {
        return values1h;
    }

    public void setValues1h(List<SensorValue> values1h) {
        this.values1h = values1h;
    }

    public List<SensorValue> getValues1d() {
        return values1d;
    }

    public void setValues1d(List<SensorValue> values1d) {
        this.values1d = values1d;
    }

    public List<SensorValue> getValues1w() {
        return values1w;
    }

    public void setValues1w(List<SensorValue> values1w) {
        this.values1w = values1w;
    }

    public List<SensorValue> getValues1m() {
        return values1m;
    }

    public void setValues1m(List<SensorValue> values1m) {
        this.values1m = values1m;
    }
}
