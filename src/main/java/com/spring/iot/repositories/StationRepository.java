package com.spring.iot.repositories;

import com.spring.iot.entities.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StationRepository extends JpaRepository<Station,String> {
    Station findStationById(String id);
    List<Station> getStationsByActive (Boolean active);


    @Query("select sv.id,st.name,s.id,sv.value,sv.timeUpdate" +
            " from Station st join Sensor s on st.id = s.station.id join SensorValue sv on sv.sensor.id = s.id")
    List<List<Object>> getListValue();
}
