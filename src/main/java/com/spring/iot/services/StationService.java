package com.spring.iot.services;


import com.spring.iot.entities.Station;
import com.spring.iot.repositories.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationService {
    @Autowired
    private StationRepository stationRepository;

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


}
