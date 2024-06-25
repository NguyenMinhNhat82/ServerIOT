package com.spring.iot.repositories;

import com.spring.iot.entities.Sensor;
import com.spring.iot.entities.SensorValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface SensorValueRepository extends JpaRepository<SensorValue,Integer> {
    @Query(nativeQuery = true,value = "SELECT * FROM public.sensor_value where sensor_id = :id\n" +
            "ORDER BY time_update ASC ")
    List<SensorValue> getListValueBySensor(@Param("id") String id);

    @Query(nativeQuery = true,value = "SELECT * FROM public.sensor_value where sensor_id = :id\n" +
            "ORDER BY time_update DESC ")
    List<SensorValue> getListValueBySensorCurrent(@Param("id") String id);
    List<SensorValue> getSensorValuesBySensor_Id(String id);
    List<SensorValue> findFirstBySensor_IdOrderByTimeUpdateDesc(String id);

    List<SensorValue> findBySensorAndTimeUpdateBetween(Sensor sensor,LocalDateTime startDateTime, LocalDateTime endDateTime);



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


    @Query(nativeQuery = true,
            value = "SELECT *  FROM public.sensor_value\n" +
                    "where EXTRACT(EPOCH FROM (timezone('Asia/Saigon', CURRENT_TIMESTAMP) - public.sensor_value.time_update)) <=:time \n" +
                    "and sensor_id = :idSensor \n" +
                    "ORDER BY public.sensor_value.time_update ASC ")
    List<SensorValue> getSensorValueByBetweenTime(@Param("idSensor") String idSensor ,@Param("time") Integer time);

    @Query("SELECT YEAR(sv.timeUpdate) AS year, " +
            "MONTH(sv.timeUpdate) AS month, " +
            "WEEK(sv.timeUpdate) AS week, " +
            "SUM(CAST(sv.value AS double)) AS totalValue " +
            "FROM SensorValue sv " +
            "WHERE YEAR(sv.timeUpdate) = :year " +
            "AND MONTH(sv.timeUpdate) = :month " +
            "GROUP BY YEAR(sv.timeUpdate), MONTH(sv.timeUpdate), WEEK(sv.timeUpdate)")
    List<Map<String, Object>> getDataByMonthGroupByWeek(@Param("year") int year, @Param("month") int month);

    @Query("SELECT sv FROM SensorValue sv " +
            "WHERE sv.sensor = :sensor " +
            "AND sv.timeUpdate >= :startTime " +
            "ORDER BY sv.timeUpdate DESC")
    List<SensorValue> findLatestBySensorAndTimeUpdate(@Param("sensor") Sensor sensor,
                                                      @Param("startTime") LocalDateTime startTime);
}
