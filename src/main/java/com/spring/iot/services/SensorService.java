package com.spring.iot.services;

import com.spring.iot.entities.Sensor;
import com.spring.iot.entities.Station;
import com.spring.iot.repositories.SensorRepository;
import com.spring.iot.repositories.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SensorService {
    @Autowired
    SensorRepository sensorRepository;

    @Autowired
    StationRepository stationRepository;

    public Sensor addOrUpdate(Sensor sensor){
        return sensorRepository.save(sensor);
    }

    public Sensor findSensorByID(String id){
        return sensorRepository.findSensorById(id);
    }

    public List<Sensor> getListSensorByStation(String idStation){
        Station s = stationRepository.findStationById(idStation);
        List<Sensor> list  = sensorRepository.getSensorByStation_Id(idStation);
        List<Sensor> listRemove  = new ArrayList<>();
        for(Sensor ss :list ){
            if(ss.getId().split("_")[0].equals("Relay")){
                listRemove.add(ss);
            }
        }
        list.removeAll(listRemove);
        return list;
    }

}
