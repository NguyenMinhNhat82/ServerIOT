package com.spring.iot.repositories;

import com.spring.iot.entities.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor, String> {
    Sensor findSensorById(String id);
    List<Sensor> getSensorByStation_Id(String id);
}
