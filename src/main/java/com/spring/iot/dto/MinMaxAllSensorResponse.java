package com.spring.iot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


public class MinMaxAllSensorResponse {
    private List<SensorMinMax> sensorMinMaxes;

    public MinMaxAllSensorResponse(List<SensorMinMax> sensorMinMaxes) {
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
        List<ValueHour> data;

        public SensorMinMax(String sensor, List<ValueHour> data) {
            this.sensor = sensor;
            this.data = data;
        }

        public String getSensor() {
            return sensor;
        }

        public void setSensor(String sensor) {
            this.sensor = sensor;
        }

        public List<ValueHour> getData() {
            return data;
        }

        public void setData(List<ValueHour> data) {
            this.data = data;
        }
    }

    public static class ValueHour {
        private String hour;
        private String min;
        private String max;
        private LocalDateTime minAt;
        private LocalDateTime maxAt;

        public ValueHour(String hour, String min, String max, LocalDateTime minAt, LocalDateTime maxAt) {
            this.hour = hour;
            this.min = min;
            this.max = max;
            this.minAt = minAt;
            this.maxAt = maxAt;
        }

        public String getHour() {
            return hour;
        }

        public void setHour(String hour) {
            this.hour = hour;
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
