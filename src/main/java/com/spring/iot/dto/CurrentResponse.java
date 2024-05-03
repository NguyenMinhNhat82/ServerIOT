package com.spring.iot.dto;


import com.spring.iot.entities.SensorValue;
import lombok.*;
import org.hibernate.internal.build.AllowSysOut;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CurrentResponse {
    private List<ValueOfSensor> tempValue;
    private List<ValueOfSensor> humiValue;
    private List<ValueOfSensor> phValue;
    private List<ValueOfSensor> ecValue;
    private List<ValueOfSensor> kaliValue;
    private List<ValueOfSensor> nitoValue;
    private List<ValueOfSensor> photphoValue;
    private List<RelayValue> relayValues;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ValueOfSensor{
        private String name;
        private String value;
        private Integer state;
        private String changeValue;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RelayValue{
        private String name;
        private String value;
    }


}
