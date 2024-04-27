package com.spring.iot.controllers;


import com.spring.iot.dto.MinMaxAllSensorResponse;
import com.spring.iot.dto.MinMaxResponse;
import com.spring.iot.entities.Sensor;
import com.spring.iot.entities.SensorValue;
import com.spring.iot.entities.Station;
import com.spring.iot.services.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.spring.iot.util.Utils.historyValue;

@RestController
public class StationController {

    @Autowired
    private StationService stationService;
    @Autowired
    private UserService userService;
    @Autowired
    private SensorValueService sensorValueService;

    @Autowired
    private SensorService sensorService;

    @GetMapping("/data")
    @CrossOrigin
    ResponseEntity<Map<String, Station>> getdata(){
        Map<String, Station> s = historyValue;
        return new ResponseEntity<>(historyValue, HttpStatus.OK);
    }

    @GetMapping("/api/all-station")
    @CrossOrigin
    ResponseEntity<List<Station>> getAllStation(){
        return new ResponseEntity<>(stationService.getAllStation(),HttpStatus.OK);
    }

    @GetMapping("/api/station/sensor/{idStation}")
    @CrossOrigin
    ResponseEntity<List<Sensor>> getListSensorByStation(@PathVariable("idStation") String idStation){
        return new ResponseEntity<>(sensorService.getListSensorByStation(idStation), HttpStatus.OK);
    }

    @GetMapping("/api/staion-info/{id}")
    ResponseEntity<Station> getInfoStion(@PathVariable("id") String id){
        return new ResponseEntity<>(stationService.findStattionByID(id), HttpStatus.OK);
    }

    @GetMapping("/api/value-sensor/{nameStation}")
    @CrossOrigin
    ResponseEntity<List<SensorValue>> getvalueSensor(@PathVariable String nameStation){
        return new ResponseEntity<>(sensorValueService.CurrentDataSensor(nameStation),HttpStatus.OK);
    }
    @GetMapping("/api/value-sensor/{value}/station/{station}")
    @CrossOrigin
    ResponseEntity<List<SensorValue>> getCurrentRelay(@PathVariable("station") String station, @PathVariable("value") String value){
        return new ResponseEntity<>(sensorValueService.getCurrentListOfRelay(station,value), HttpStatus.OK);
    }

    @GetMapping("/api/value-sensor-1h/{nameSensor}")
    @CrossOrigin
    ResponseEntity<List<SensorValue>> getvalueSensor1h(@PathVariable String nameSensor){
        return new ResponseEntity<>(sensorValueService.DataSensorHour(nameSensor),HttpStatus.OK);
    }
    @GetMapping("/api/value-sensor-1d/{nameSensor}")
    @CrossOrigin
    ResponseEntity<List<SensorValue>> getvalueSensor1d(@PathVariable String nameSensor){
        return new ResponseEntity<>(sensorValueService.DataSensorDay(nameSensor),HttpStatus.OK);
    }
    @GetMapping("/api/value-sensor-1w/{nameSensor}")
    @CrossOrigin
    ResponseEntity<List<SensorValue>> getvalueSensor1w(@PathVariable String nameSensor){
        return new ResponseEntity<>(sensorValueService.DataSensorWeek(nameSensor),HttpStatus.OK);
    }
    @GetMapping("/api/value-sensor-1m/{nameSensor}")
    @CrossOrigin
    ResponseEntity<List<SensorValue>> getvalueSensor1m(@PathVariable String nameSensor){
        return new ResponseEntity<>(sensorValueService.DataSensorMonth(nameSensor),HttpStatus.OK);
    }
//    @GetMapping("/api/max-value-sensor-1h/{nameSensor}")
//    @CrossOrigin
//    ResponseEntity<Integer> getMaxvalueSensor1h(@PathVariable String nameSensor){
//        return new ResponseEntity<>(sensorValueService.MaxSensorHour(nameSensor),HttpStatus.OK);
//    }
//    @GetMapping("/api/max-value-sensor-1d/{nameSensor}")
//    @CrossOrigin
//    ResponseEntity<Integer> getMaxvalueSensor1d(@PathVariable String nameSensor){
//        return new ResponseEntity<>(sensorValueService.MaxSensorDay(nameSensor),HttpStatus.OK);
//    }
//    @GetMapping("/api/max-value-sensor-1w/{nameSensor}")
//    @CrossOrigin
//    ResponseEntity<Integer> getMaxvalueSensor1w(@PathVariable String nameSensor){
//        return new ResponseEntity<>(sensorValueService.MaxSensorWeek(nameSensor),HttpStatus.OK);
//    }
//    @GetMapping("/api/max-value-sensor-1m/{nameSensor}")
//    @CrossOrigin
//    ResponseEntity<Integer> getMaxvalueSensor1m(@PathVariable String nameSensor){
//        return new ResponseEntity<>(sensorValueService.MaxSensorMonth(nameSensor),HttpStatus.OK);
//    }
//    @GetMapping("/api/min-value-sensor-1h/{nameSensor}")
//    @CrossOrigin
//    ResponseEntity<Integer> getMinvalueSensor1h(@PathVariable String nameSensor){
//        return new ResponseEntity<>(sensorValueService.MinSensorHour(nameSensor),HttpStatus.OK);
//    }
//    @GetMapping("/api/min-value-sensor-1d/{nameSensor}")
//    @CrossOrigin
//    ResponseEntity<Integer> getMinvalueSensor1d(@PathVariable String nameSensor){
//        return new ResponseEntity<>(sensorValueService.MinSensorDay(nameSensor),HttpStatus.OK);
//    }
//    @GetMapping("/api/min-value-sensor-1w/{nameSensor}")
//    @CrossOrigin
//    ResponseEntity<Integer> getMinvalueSensor1w(@PathVariable String nameSensor){
//        return new ResponseEntity<>(sensorValueService.MinSensorWeek(nameSensor),HttpStatus.OK);
//    }
//    @GetMapping("/api/min-value-sensor-1m/{nameSensor}")
//    @CrossOrigin
//    ResponseEntity<Integer> getMinvalueSensor1m(@PathVariable String nameSensor){
//        return new ResponseEntity<>(sensorValueService.MinSensorMonth(nameSensor),HttpStatus.OK);
//    }
//
//    @GetMapping("/api/current/{id}")
//    @CrossOrigin
//    ResponseEntity<Station> currentStatusStation(@PathVariable String id){
//        return  new ResponseEntity<>(stationService.findStattionByID(id),HttpStatus.OK);
//    }

    @GetMapping("/api/min-max-value/{nameSensor}")
    ResponseEntity<MinMaxResponse> getMinMaxBySensor(@PathVariable("nameSensor") String nameSensor){
        return new ResponseEntity<>(sensorValueService.minMaxResponse(nameSensor), HttpStatus.OK);
    }

    @PostMapping("/api/all-min-max/{stationId}")
    ResponseEntity<MinMaxAllSensorResponse> getMinMax(
            @PathVariable("stationId") String station,
            @RequestBody Map<String,String> req
    ) throws Exception {
        return  new ResponseEntity<>(sensorValueService.getMinMaxOfSenSortInStation(req.get("date"),station), HttpStatus.OK);
    }


}
