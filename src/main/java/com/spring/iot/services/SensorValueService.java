package com.spring.iot.services;

import com.spring.iot.dto.MinMaxResponse;
import com.spring.iot.entities.Sensor;
import com.spring.iot.entities.SensorValue;
import com.spring.iot.repositories.SensorRepository;
import com.spring.iot.repositories.SensorValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class SensorValueService {
    @Autowired
    private SensorValueRepository sensorValueRepository;
    @Autowired
    private SensorRepository sensorRepository;
    public SensorValue addOrUpdate(SensorValue sensorValue){
        List<SensorValue> sensorValueList = sensorValueRepository.getListValueBySensor(sensorValue.getSensor().getId());
        if(sensorValueList.size() == 0)
            return sensorValueRepository.save(sensorValue);
        LocalDateTime fromDate = sensorValueList.get(0).getTimeUpdate();
        LocalDateTime toDate = sensorValue.getTimeUpdate();
        Duration duration = Duration.between(fromDate,toDate);
        if(duration.getSeconds() > 3600 *24 *31){
            sensorValueRepository.delete(sensorValueList.get(0));
        }
        return sensorValueRepository.save(sensorValue);
    }

    public List<SensorValue> listValue(String idSensor){
        return  sensorValueRepository.getListValueBySensor(idSensor);
    }
    public  List<SensorValue> CurrentDataSensor (String station)
    {
        List<SensorValue> list = new ArrayList<>();
        List<Sensor> s = sensorRepository.getSensorByStation_Id(station);
        for (Sensor v : s) {
            list.addAll(sensorValueRepository.findFirstBySensor_IdOrderByTimeUpdateDesc(v.getId()));
        }
        return list;
    }
    public List<SensorValue> sensorValueList(){
        return sensorValueRepository.findAll();
    }

    public List<SensorValue> getCurrentListOfRelay(String station, String value){
        List<SensorValue> arr = new ArrayList<>();
        for(SensorValue s: this.CurrentDataSensor(station)){
            if(s.getSensor().getId().split("_")[0].equals(value)){
                arr.add(s);
            }
        }
        return arr;
    }
    public List<SensorValue> DataSensorHour (String idsensor)
    {
        List<SensorValue> newlist = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now();
        List<SensorValue> value = sensorValueRepository.getSensorValuesBySensor_Id(idsensor);
        for (SensorValue s : value) {
            LocalDateTime timeUpdate = s.getTimeUpdate();
            long secondsDifference = ChronoUnit.SECONDS.between(timeUpdate, currentTime);
            if (secondsDifference <= 3600) {
                newlist.add(s);
            }
        }
        return newlist;
    }
    public List<SensorValue> DataSensorDay (String idsensor)
    {
        List<SensorValue> newlist = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now();
        List<SensorValue> value = sensorValueRepository.getSensorValuesBySensor_Id(idsensor);
        for (SensorValue s : value) {
            LocalDateTime timeUpdate = s.getTimeUpdate();
            long secondsDifference = ChronoUnit.SECONDS.between(timeUpdate, currentTime);
            if (secondsDifference <= 86400) {
                newlist.add(s);
            }
        }
        return newlist;
    }


    public List<SensorValue> DataAllSensorInDay ()
    {
        List<SensorValue> newlist = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now();
        List<SensorValue> value = sensorValueRepository.findAll();
        for (SensorValue s : value) {
            LocalDateTime timeUpdate = s.getTimeUpdate();
            long secondsDifference = ChronoUnit.SECONDS.between(timeUpdate, currentTime);
            if (secondsDifference <= 86400) {
                newlist.add(s);
            }
        }
        return newlist;
    }
    public List<SensorValue> DataSensorWeek (String idsensor)
    {
        List<SensorValue> newlist = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now();
        List<SensorValue> value = sensorValueRepository.getSensorValuesBySensor_Id(idsensor);
        for (SensorValue s : value) {
            LocalDateTime timeUpdate = s.getTimeUpdate();
            long secondsDifference = ChronoUnit.SECONDS.between(timeUpdate, currentTime);
            if (secondsDifference <= 604800) {
                newlist.add(s);
            }
        }
        return newlist;
    }
    public List<SensorValue> DataSensorMonth (String idsensor)
    {
        List<SensorValue> newlist = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now();
        List<SensorValue> value = sensorValueRepository.getSensorValuesBySensor_Id(idsensor);
        for (SensorValue s : value) {
            LocalDateTime timeUpdate = s.getTimeUpdate();
            long secondsDifference = ChronoUnit.SECONDS.between(timeUpdate, currentTime);
            if (secondsDifference <= 2592000) {
                newlist.add(s);
            }
        }
        return newlist;
    }

    public String MaxSensorHour (String idsensor)
    {
        Double max = 0.0;
        SensorValue value = new SensorValue();
        for (SensorValue s : DataSensorHour(idsensor))
        {
            if (Double.parseDouble(s.getValue()) > max)
            {
                max = Double.parseDouble(s.getValue());
            }
        }

        return max.toString();
    }
    public String MaxSensorDay (String idsensor)
    {
        Double max = 0.0;
        SensorValue value = new SensorValue();
        for (SensorValue s : DataSensorDay(idsensor))
        {
            if (Double.parseDouble(s.getValue()) > max)
            {
                max = Double.parseDouble(s.getValue());
            }
        }

        return max.toString();
    }
    public String MaxSensorWeek (String idsensor)
    {
        Double max = 0.0;
        SensorValue value = new SensorValue();
        for (SensorValue s : DataSensorWeek(idsensor))
        {
            if (Double.parseDouble(s.getValue()) > max)
            {
                max = Double.parseDouble(s.getValue());
            }
        }

        return max.toString();
    }
    public String MaxSensorMonth (String idsensor)
    {
        Double max = 0.0;
        SensorValue value = new SensorValue();
        for (SensorValue s : DataSensorMonth(idsensor))
        {
            if (Double.parseDouble(s.getValue()) > max)
            {
                max = Double.parseDouble(s.getValue());
            }
        }

        return max.toString();
    }
    public String MinSensorHour (String idsensor)
    {
        Double min = 9999999.0;
        SensorValue value = new SensorValue();
        for (SensorValue s : DataSensorHour(idsensor))
        {
            if (Double.parseDouble(s.getValue()) < min)
            {
                min = Double.parseDouble(s.getValue());
            }
        }

        return min.toString();
    }
    public String MinSensorDay (String idsensor)
    {
        Double min = 9999999.0;
        SensorValue value = new SensorValue();
        for (SensorValue s : DataSensorDay(idsensor))
        {
            if (Double.parseDouble(s.getValue()) < min)
            {
                min = Double.parseDouble(s.getValue());
            }
        }

        return min.toString();
    }
    public String  MinSensorWeek (String idsensor)
    {
        Double min = 9999999.0;
        SensorValue value = new SensorValue();
        for (SensorValue s : DataSensorWeek(idsensor))
        {
            if (Double.parseDouble(s.getValue()) < min)
            {
                min = Double.parseDouble(s.getValue());
            }
        }

        return min.toString();
    }
    public String MinSensorMonth (String idsensor)
    {
        Double min = 9999999.0;
        SensorValue value = new SensorValue();
        for (SensorValue s : DataSensorMonth(idsensor))
        {
            if (Double.parseDouble(s.getValue()) < min)
            {
                min = Double.parseDouble(s.getValue());
            }
        }

        return min.toString();
    }

    public MinMaxResponse minMaxResponse(String idSensor){
        MinMaxResponse minMax = new MinMaxResponse();
        minMax.setMax1h(this.MaxSensorHour(idSensor));
        minMax.setMax1d(this.MaxSensorDay(idSensor));
        minMax.setMax1w(this.MaxSensorWeek(idSensor));
        minMax.setMax1m(this.MaxSensorMonth(idSensor));
        minMax.setMin1h(this.MinSensorHour(idSensor));
        minMax.setMin1d(this.MinSensorDay(idSensor));
        minMax.setMin1w(this.MinSensorWeek(idSensor));
        minMax.setMin1m(this.MinSensorMonth(idSensor));
        return minMax;
    }


}
