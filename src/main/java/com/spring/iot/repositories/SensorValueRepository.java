package com.spring.iot.repositories;

import com.spring.iot.entities.SensorValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SensorValueRepository extends JpaRepository<SensorValue,Integer> {
    @Query(nativeQuery = true,value = "SELECT * FROM public.sensor_value where sensor_id = :id\n" +
            "ORDER BY time_update ASC ")
    List<SensorValue> getListValueBySensor(@Param("id") String id);

    @Query(nativeQuery = true,value = "SELECT * FROM public.sensor_value where sensor_id = :id\n" +
            "ORDER BY time_update DESC ")
    List<SensorValue> getListValueBySensorCurrent(@Param("id") String id);
    List<SensorValue> getSensorValuesBySensor_Id(String id);
    List<SensorValue> findFirstBySensor_IdOrderByTimeUpdateDesc(String id);



    @Query("SELECT sv from SensorValue  sv where" +
            " year(sv.timeUpdate) = :year and" +
            " month(sv.timeUpdate) = :month and" +
            " day(sv.timeUpdate) = :day " +
            "and sv.sensor.station.id = :idStation" +
            " and sv.sensor.id  = :idSensor and hour(sv.timeUpdate) =:hour")
    List<SensorValue> findAllSensorValueByDate(
            @Param("day") String date,
            @Param("month") String month,
            @Param("year") String year ,
            @Param("idStation") String idStation,
            @Param("idSensor") String idSensor,
            @Param("hour") String hour);

}
