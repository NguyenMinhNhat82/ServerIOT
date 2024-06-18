package com.spring.iot.controllers;


import com.spring.iot.dto.*;
import com.spring.iot.entities.Sensor;
import com.spring.iot.entities.SensorValue;
import com.spring.iot.entities.Station;
import com.spring.iot.services.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    ResponseEntity<CurrentResponse> getvalueSensor(@PathVariable String nameStation){
        return new ResponseEntity<>(sensorValueService.currentData(nameStation),HttpStatus.OK);
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


    @GetMapping("/export")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=parameters_of_the_soil_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<SensorValue> listValue = sensorValueService.DataAllSensorInDay();
        List<Station> stations = stationService.getAll();
        List<Sensor> sensors = sensorService.getAll();

        ExcelService excelExporter = new ExcelService(listValue,stations,sensors);

        excelExporter.export(response);
    }

    @GetMapping("/api/history-of-sensor/{sensorId}")
    public ResponseEntity<HistoryDataResponse> historyDataResponse(@PathVariable("sensorId") String sensorId){
        return  new ResponseEntity<>(sensorValueService.getAllHistoryDataOfSensor(sensorId), HttpStatus.OK);
    }

    @GetMapping("/api/all-station-and-sensor")
    public ResponseEntity<StationAndSensorResponse> getAllStationAndSensor(){
        return new ResponseEntity<>(sensorService.getAllStatInAndSensor(), HttpStatus.OK);
    }
    @GetMapping("/api/station/in-active/{idStation}")
    public ResponseEntity<String> inActiveStation(@PathVariable("idStation") String idStation){
        return new ResponseEntity<>(stationService.inActiveStaion(idStation), HttpStatus.OK);
    }
    @GetMapping("/api/station/active/{idStation}")
    public ResponseEntity<String> activeStation(@PathVariable("idStation") String idStation){
        return new ResponseEntity<>(stationService.activeStaion(idStation), HttpStatus.OK);
    }
    @GetMapping("/api/sensor/avtive/{idSensor}")
    public ResponseEntity<String> activeSensor(@PathVariable("idSensor") String idSensor){
        return new ResponseEntity<>(sensorService.activeSensor(idSensor), HttpStatus.OK);
    }
    @GetMapping("/api/sensor/in-avtive/{idSensor}")
    public ResponseEntity<String> inActiveSensor(@PathVariable("idSensor") String idSensor){
        return new ResponseEntity<>(sensorService.inActiveSensor(idSensor), HttpStatus.OK);
    }

    @GetMapping("/api/sensor/average/{idSensor}")
    public ResponseEntity<AverageValueResponse> getAverageOfSensor(@PathVariable("idSensor") String idSensor){
        return new ResponseEntity<>(sensorValueService.averageValueResponse(idSensor), HttpStatus.OK);
    }

    @PostMapping("/api/sensor/schedule-inactive")
    public ResponseEntity<String> scheduleInActive(@RequestBody ScheduleRequest scheduleRequest){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        if(scheduleRequest.getTime() != null && !scheduleRequest.getTime().isEmpty()){
            LocalTime parsedTime = LocalTime.parse(scheduleRequest.getTime(), formatter);
            LocalDateTime now  = LocalDateTime.now();
            LocalDateTime timeToSchedule =  now.withHour(parsedTime.getHour()).withMinute(parsedTime.getMinute()).withSecond(0).withNano(0);
           if(timeToSchedule.isBefore(now)){
               timeToSchedule = timeToSchedule.plusDays(1);
           }
           try {
               sensorService.scheduleTimeToInactive(timeToSchedule, scheduleRequest.getIdSensor());
               return new ResponseEntity<>("Success", HttpStatus.OK);

           }
           catch (Exception ex){
               return new ResponseEntity<>(ex.getMessage(), HttpStatus.OK);

           }

        }
        else
            return new ResponseEntity<>("Time can be blank", HttpStatus.OK);
    }

    @PostMapping("/api/sensor/edit-schedule-inactive/{taskID}")
    public ResponseEntity<String> editScheduleInActive(@RequestBody ScheduleRequest scheduleRequest,
                                                       @PathVariable("taskID") Integer taskID){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        if(scheduleRequest.getTime() != null && !scheduleRequest.getTime().isEmpty()){
            LocalTime parsedTime = LocalTime.parse(scheduleRequest.getTime(), formatter);
            LocalDateTime now  = LocalDateTime.now();
            LocalDateTime timeToSchedule =  now.withHour(parsedTime.getHour()).withMinute(parsedTime.getMinute()).withSecond(0).withNano(0);
            if(timeToSchedule.isBefore(now)){
                timeToSchedule = timeToSchedule.plusDays(1);
            }
            try {
                sensorService.editScheduledTask(taskID,timeToSchedule, scheduleRequest.getIdSensor());
                return new ResponseEntity<>("Success", HttpStatus.OK);

            }
            catch (Exception ex){
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.OK);

            }

        }
        else
            return new ResponseEntity<>("Time can be blank", HttpStatus.OK);
    }

    @GetMapping("/api/sensor/cancel-schedule/{idSensor}/{taskID}")
    public ResponseEntity<String> cancelSchedule(@PathVariable("idSensor") String idSensor,
                                                 @PathVariable("taskID") Integer taskId){
        return new ResponseEntity<>(sensorService.cancelScheduledTask(taskId,idSensor), HttpStatus.OK);
    }

    @GetMapping("/api/sensor/dataByMonthAndWeek/{idStation}")
    public ResponseEntity<MinMaxSensorByMonth> getDataByMonthAndWeek(
            @RequestParam int year,
            @RequestParam int month,
            @PathVariable("idStation") String idStation) {
        return new ResponseEntity<>(sensorValueService.getMinAndMaxValueInAllWeekByMonth(year, month,idStation), HttpStatus.OK);
    }
    @GetMapping("/api/sensor/dataByWeek/{idStation}")
    public ResponseEntity<MinMaxSensorByWeek> getDataByWeekAndDay(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int index,
            @PathVariable("idStation") String idStation) {
        return new ResponseEntity<>(sensorValueService.getMinAndMaxValueInAllWeekByWeek(year, month,idStation,index), HttpStatus.OK);
    }

    @GetMapping("/api/sensor/get-index")
    public ResponseEntity<Integer> getIndex(
            @RequestParam int year,
            @RequestParam int month){
        return new ResponseEntity<>(sensorValueService.getAllWeeksInMonth(month,year).size(), HttpStatus.OK);
    }







}
