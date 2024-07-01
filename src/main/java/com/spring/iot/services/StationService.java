package com.spring.iot.services;


import com.spring.iot.dto.Status;
import com.spring.iot.entities.Sensor;
import com.spring.iot.entities.Station;
import com.spring.iot.repositories.SensorRepository;
import com.spring.iot.repositories.StationRepository;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class StationService {
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private SensorRepository sensorRepository;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private String urlFetch  = "https://iotcontroller-1.onrender.com/";

    public Station addOrUpdate(Station station){
        return stationRepository.save(station);
    }
    public List<Station> getAll(){return stationRepository.findAll();}

    public Station findStattionByID(String id){
        return stationRepository.findStationById(id);
    }
    public List<Station> getAllStation(){
        return stationRepository.findAll();
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
                Calendar cal = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                com.spring.iot.dto.Message m = new com.spring.iot.dto.Message("server", "client", "Update", dateFormat.format(cal.getTime()), Status.MESSAGE);
                simpMessagingTemplate.convertAndSendToUser(m.getReceiverName(), "/private", m);
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
                Calendar cal = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                com.spring.iot.dto.Message m = new com.spring.iot.dto.Message("server", "client", "Update", dateFormat.format(cal.getTime()), Status.MESSAGE);
                simpMessagingTemplate.convertAndSendToUser(m.getReceiverName(), "/private", m);
                return "Success";
            }
            else
                return  "Failed";
        }catch (Exception exception){
            return  exception.getMessage();
        }

    }
}
