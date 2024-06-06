package com.spring.iot.dto;


import com.spring.iot.entities.SensorValue;
import lombok.*;
import org.hibernate.internal.build.AllowSysOut;

import java.util.List;

@Data
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

    public List<ValueOfSensor> getTempValue() {
        return tempValue;
    }

    public void setTempValue(List<ValueOfSensor> tempValue) {
        this.tempValue = tempValue;
    }

    public List<ValueOfSensor> getHumiValue() {
        return humiValue;
    }

    public void setHumiValue(List<ValueOfSensor> humiValue) {
        this.humiValue = humiValue;
    }

    public List<ValueOfSensor> getPhValue() {
        return phValue;
    }

    public void setPhValue(List<ValueOfSensor> phValue) {
        this.phValue = phValue;
    }

    public List<ValueOfSensor> getEcValue() {
        return ecValue;
    }

    public void setEcValue(List<ValueOfSensor> ecValue) {
        this.ecValue = ecValue;
    }

    public List<ValueOfSensor> getKaliValue() {
        return kaliValue;
    }

    public void setKaliValue(List<ValueOfSensor> kaliValue) {
        this.kaliValue = kaliValue;
    }

    public List<ValueOfSensor> getNitoValue() {
        return nitoValue;
    }

    public void setNitoValue(List<ValueOfSensor> nitoValue) {
        this.nitoValue = nitoValue;
    }

    public List<ValueOfSensor> getPhotphoValue() {
        return photphoValue;
    }

    public void setPhotphoValue(List<ValueOfSensor> photphoValue) {
        this.photphoValue = photphoValue;
    }

    public List<RelayValue> getRelayValues() {
        return relayValues;
    }

    public void setRelayValues(List<RelayValue> relayValues) {
        this.relayValues = relayValues;
    }

    public CurrentResponse() {
    }

    public CurrentResponse(List<ValueOfSensor> tempValue, List<ValueOfSensor> humiValue, List<ValueOfSensor> phValue, List<ValueOfSensor> ecValue, List<ValueOfSensor> kaliValue, List<ValueOfSensor> nitoValue, List<ValueOfSensor> photphoValue, List<RelayValue> relayValues) {
        this.tempValue = tempValue;
        this.humiValue = humiValue;
        this.phValue = phValue;
        this.ecValue = ecValue;
        this.kaliValue = kaliValue;
        this.nitoValue = nitoValue;
        this.photphoValue = photphoValue;
        this.relayValues = relayValues;
    }

    @Data
    public static class ValueOfSensor{
        private String name;
        private String value;
        private Integer state;
        private String changeValue;

        public ValueOfSensor(String name, String value, Integer state, String changeValue) {
            this.name = name;
            this.value = value;
            this.state = state;
            this.changeValue = changeValue;
        }

        public ValueOfSensor() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Integer getState() {
            return state;
        }

        public void setState(Integer state) {
            this.state = state;
        }

        public String getChangeValue() {
            return changeValue;
        }

        public void setChangeValue(String changeValue) {
            this.changeValue = changeValue;
        }
    }
    @Data
    @NoArgsConstructor
    public static class RelayValue{
        private String name;
        private String value;

        public RelayValue(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


}
