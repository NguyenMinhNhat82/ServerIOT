package com.spring.iot.services;

import com.spring.iot.dto.StationAndSensorResponse;
import com.spring.iot.entities.Sensor;
import com.spring.iot.entities.Station;
import com.spring.iot.repositories.SensorRepository;
import com.spring.iot.repositories.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.stylesheets.LinkStyle;

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
    public StationAndSensorResponse getAllStatInAndSensor(){
        StationAndSensorResponse stationAndSensorResponse = new StationAndSensorResponse();
        List<Station> listStations = stationRepository.findAll();
        List<StationAndSensorResponse.SensorByStation> res = new ArrayList<>();
        for(Station s : listStations ){
            StationAndSensorResponse.SensorByStation  i = new StationAndSensorResponse.SensorByStation();
            i.setActive(s.getActive());
            i.setIdStation(s.getId());
            i.setNameStation(s.getName());
            List<Sensor> listSensor = sensorRepository.getSensorByStation_Id(s.getId());
            List<StationAndSensorResponse.SensorByStation.SensorData> sData = new ArrayList<>();
            for(Sensor sensor : listSensor ){
                StationAndSensorResponse.SensorByStation.SensorData data = new StationAndSensorResponse.SensorByStation.SensorData();
                data.setIdSensor(sensor.getId());
                data.setActive(sensor.getActive());
                sData.add(data);
            }
            i.setListSensor(sData);
            res.add(i);

        }
        stationAndSensorResponse.setData(res);
        return stationAndSensorResponse;
    }
    public  List<Sensor> getAll(){
        return sensorRepository.findAll();
    }
    private String urlFetch  = "http://localhost:9001/";
    public String activeSensor(String idSensor) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            Sensor sensor = sensorRepository.findSensorById(idSensor);
            Station s = stationRepository.findStationById(sensor.getStation().getId());
            if(s.getActive() != true){
                s.setActive(true);
                stationRepository.save(s);
                ResponseEntity<String> response
                        = restTemplate.getForEntity( urlFetch + "api/turn-on-only/" + s.getId(), String.class);
                if(!response.getBody().equals("Success")) {
                    return "Failed";
                }
            }

            sensor.setActive(true);

            sensorRepository.save(sensor);

            String url =   urlFetch + "/api/turn-on/" + sensor.getStation().getId() +"/" + idSensor;
            ResponseEntity<String> response
                    = restTemplate.getForEntity(url , String.class);
            if(response.getBody().equals("Success")){
                return "Success";
            }
            else
                return  "Failed";

        }catch (Exception ex){
            return ex.getMessage();
        }
    }

    public String inActiveSensor(String idSensor) {
        try {
            Sensor sensor = sensorRepository.findSensorById(idSensor);
            sensor.setActive(false);
            sensorRepository.save(sensor);
            RestTemplate restTemplate = new RestTemplate();
            String url =   urlFetch + "/api/turn-off/" + sensor.getStation().getId() +"/" + idSensor;
            ResponseEntity<String> response
                    = restTemplate.getForEntity(url , String.class);
            if(response.getBody().equals("Success")){
                return "Success";
            }
            else
                return  "Failed";

        }catch (Exception ex){
            return ex.getMessage();
        }
    }
}
