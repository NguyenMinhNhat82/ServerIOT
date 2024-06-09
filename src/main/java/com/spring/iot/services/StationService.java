package com.spring.iot.services;


import com.spring.iot.entities.Sensor;
import com.spring.iot.entities.Station;
import com.spring.iot.repositories.SensorRepository;
import com.spring.iot.repositories.StationRepository;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class StationService {
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private SensorRepository sensorRepository;

    private String urlFetch  = "http://localhost:9001/";

    public Station addOrUpdate(Station station){
        return stationRepository.save(station);
    }
    public List<Station> getAll(){return stationRepository.findAll();}

    public Station findStattionByID(String id){
        return stationRepository.findStationById(id);
    }
    public List<Station> getAllStation(){
        return stationRepository.getStationsByActive(true);
    }
    public  List<Station> allListStaion(){
        return this.stationRepository.findAll();
    }
    public void  setNonActiveForStation(List<Station> station){
        List<Station> getAllStation = stationRepository.findAll();
        for(Station s : station){
            if(getAllStation.contains(s));
                getAllStation.remove(s);
        }
        for(Station s: getAllStation){
            s.setActive(false);
        }
        stationRepository.saveAll(getAllStation);
    }


    public String inActiveStaion(String idStation) {
        try {
            Station s = stationRepository.findStationById(idStation);
            s.setActive(false);
            stationRepository.saveAndFlush(s);
            List<Sensor> list = sensorRepository.getSensorByStation_Id(s.getId());
            for(Sensor sensor : list){
                sensor.setActive(false);
                sensorRepository.save(sensor);
            }
            RestTemplate restTemplate = new RestTemplate();
            String url =   urlFetch + "api/turn-off/" + idStation;
            ResponseEntity<String> response
                    = restTemplate.getForEntity(url , String.class);
            if(response.getBody().equals("Success")){
                return "Success";
            }
            else
                return  "Failed";
        }catch (Exception exception){
            return  exception.getMessage();
        }

    }
    public String activeStaion(String idStation) {
        try {
            Station s = stationRepository.findStationById(idStation);
            s.setActive(true);
            stationRepository.saveAndFlush(s);
            List<Sensor> list = sensorRepository.getSensorByStation_Id(s.getId());
            for(Sensor sensor : list){
                sensor.setActive(true);
                sensorRepository.save(sensor);
            }
            RestTemplate restTemplate = new RestTemplate();
            String url =   urlFetch + "api/turn-on/" + idStation;
            ResponseEntity<String> response
                    = restTemplate.getForEntity(url , String.class);
            if(response.getBody().equals("Success")){
                return "Success";
            }
            else
                return  "Failed";
        }catch (Exception exception){
            return  exception.getMessage();
        }

    }
}
