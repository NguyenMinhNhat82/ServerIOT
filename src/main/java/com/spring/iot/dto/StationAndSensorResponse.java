package com.spring.iot.dto;

import com.spring.iot.entities.SensorValue;

import java.util.List;

public class StationAndSensorResponse {
    private  List<SensorByStation> data;

    public StationAndSensorResponse(List<SensorByStation> data) {
        this.data = data;
    }

    public StationAndSensorResponse() {
    }

    public List<SensorByStation> getData() {
        return data;
    }

    public void setData(List<SensorByStation> data) {
        this.data = data;
    }

    public static class SensorByStation{
        private String idStation;
        private String nameStation;
        private Boolean active;
        private List<SensorData> listSensor;

        public SensorByStation() {
        }

        public SensorByStation(String idStation, String nameStation, Boolean active, List<SensorData> listSensor) {
            this.idStation = idStation;
            this.nameStation = nameStation;
            this.active = active;
            this.listSensor = listSensor;
        }

        public String getIdStation() {
            return idStation;
        }

        public void setIdStation(String idStation) {
            this.idStation = idStation;
        }

        public String getNameStation() {
            return nameStation;
        }

        public void setNameStation(String nameStation) {
            this.nameStation = nameStation;
        }

        public Boolean getActive() {
            return active;
        }

        public void setActive(Boolean active) {
            this.active = active;
        }

        public List<SensorData> getListSensor() {
            return listSensor;
        }

        public void setListSensor(List<SensorData> listSensor) {
            this.listSensor = listSensor;
        }

        public static  class SensorData{
            private String idSensor;

            private Boolean active;

            public SensorData(String idSensor, String sensorName, Boolean active) {
                this.idSensor = idSensor;
                this.active = active;
            }

            public SensorData() {
            }

            public String getIdSensor() {
                return idSensor;
            }

            public void setIdSensor(String idSensor) {
                this.idSensor = idSensor;
            }




            public Boolean getActive() {
                return active;
            }

            public void setActive(Boolean active) {
                this.active = active;
            }
        }


    }

}
