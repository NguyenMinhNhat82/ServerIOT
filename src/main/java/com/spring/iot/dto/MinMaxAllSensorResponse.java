package com.spring.iot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MinMaxAllSensorResponse {
    private List<SensorMinMax> sensorMinMaxes;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SensorMinMax {
        private String sensor;
        List<ValueHour> data;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ValueHour {
        private String hour;
        private String min;
        private String max;
        private LocalDateTime minAt;
        private LocalDateTime maxAt;
    }

}
