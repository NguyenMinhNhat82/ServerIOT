package com.spring.iot.dto;

import java.time.LocalDateTime;
import java.util.List;

public class MinMaxSensorByWeek {
    private Integer numberIndex;
    private List<SensorMinMax> sensorMinMaxes;

    public Integer getNumberIndex() {
        return numberIndex;
    }

    public void setNumberIndex(Integer numberIndex) {
        this.numberIndex = numberIndex;
    }

    public MinMaxSensorByWeek() {
    }

    public MinMaxSensorByWeek(Integer numberIndex, List<SensorMinMax> sensorMinMaxes) {
        this.numberIndex = numberIndex;
        this.sensorMinMaxes = sensorMinMaxes;
    }

    public List<SensorMinMax> getSensorMinMaxes() {
        return sensorMinMaxes;
    }

    public void setSensorMinMaxes(List<SensorMinMax> sensorMinMaxes) {
        this.sensorMinMaxes = sensorMinMaxes;
    }

    public static class SensorMinMax {
        private String sensor;

        List<ValueDay> data;

        public SensorMinMax() {
        }

        public SensorMinMax(String sensor, List<ValueDay> data) {
            this.sensor = sensor;
            this.data = data;
        }

        public String getSensor() {
            return sensor;
        }

        public void setSensor(String sensor) {
            this.sensor = sensor;
        }

        public List<ValueDay> getData() {
            return data;
        }

        public void setData(List<ValueDay> data) {
            this.data = data;
        }
    }

    public static class ValueDay {
        private String day;
        private String min;
        private String max;
        private LocalDateTime minAt;
        private LocalDateTime maxAt;

        public ValueDay() {
        }

        public ValueDay(String week, String min, String max, LocalDateTime minAt, LocalDateTime maxAt) {
            this.day = week;
            this.min = min;
            this.max = max;
            this.minAt = minAt;
            this.maxAt = maxAt;
        }

        public String getWeek() {
            return day;
        }

        public void setWeek(String week) {
            this.day = week;
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
