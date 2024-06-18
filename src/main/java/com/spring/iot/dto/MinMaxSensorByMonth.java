package com.spring.iot.dto;

import java.time.LocalDateTime;
import java.util.List;

public class MinMaxSensorByMonth {
    private Integer numWeek;
    private List<SensorMinMax> sensorMinMaxes;

    public MinMaxSensorByMonth(Integer numWeek, List<SensorMinMax> sensorMinMaxes) {
        this.numWeek = numWeek;
        this.sensorMinMaxes = sensorMinMaxes;
    }

    public MinMaxSensorByMonth() {
    }

    public Integer getNumWeek() {
        return numWeek;
    }

    public void setNumWeek(Integer numWeek) {
        this.numWeek = numWeek;
    }

    public MinMaxSensorByMonth(List<SensorMinMax> sensorMinMaxes) {
        this.sensorMinMaxes = sensorMinMaxes;
    }

    public void setSensorMinMaxes(List<SensorMinMax> sensorMinMaxes) {
        this.sensorMinMaxes = sensorMinMaxes;
    }

    public List<SensorMinMax> getSensorMinMaxes() {
        return sensorMinMaxes;
    }



    public static class SensorMinMax {
        private String sensor;
        List<ValueWeek> data;

        public SensorMinMax() {
        }

        public SensorMinMax(String sensor, List<ValueWeek> data) {
            this.sensor = sensor;
            this.data = data;
        }

        public String getSensor() {
            return sensor;
        }

        public void setSensor(String sensor) {
            this.sensor = sensor;
        }

        public List<ValueWeek> getData() {
            return data;
        }

        public void setData(List<ValueWeek> data) {
            this.data = data;
        }
    }

    public static class ValueWeek {
        private String week;
        private String min;
        private String max;
        private LocalDateTime minAt;
        private LocalDateTime maxAt;

        public ValueWeek() {
        }

        public ValueWeek(String week, String min, String max, LocalDateTime minAt, LocalDateTime maxAt) {
            this.week = week;
            this.min = min;
            this.max = max;
            this.minAt = minAt;
            this.maxAt = maxAt;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public String getMin() {
            return min;
        }

        public void setMin(String min) {
            this.min = min;
        }

        public String getMax() {
            return max;
        }

        public void setMax(String max) {
            this.max = max;
        }

        public LocalDateTime getMinAt() {
            return minAt;
        }

        public void setMinAt(LocalDateTime minAt) {
            this.minAt = minAt;
        }

        public LocalDateTime getMaxAt() {
            return maxAt;
        }

        public void setMaxAt(LocalDateTime maxAt) {
            this.maxAt = maxAt;
        }
    }

}
