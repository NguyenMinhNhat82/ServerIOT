package com.spring.iot.services;

import com.spring.iot.dto.StationAndSensorResponse;
import com.spring.iot.dto.Status;
import com.spring.iot.entities.Notification;
import com.spring.iot.entities.Sensor;
import com.spring.iot.entities.SensorValue;
import com.spring.iot.entities.Station;
import com.spring.iot.repositories.NotificationRepository;
import com.spring.iot.repositories.SensorRepository;
import com.spring.iot.repositories.StationRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class SensorService {
    @Autowired
    SensorRepository sensorRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    NotificationRepository notificationRepository;

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
                data.setSchedule(sensor.getSchedule());
                data.setTaskID(sensor.getTaskID());
                data.setTimeSchedule(sensor.getTimeSchedule());
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
    private String urlFetch  = "https://iotcontroller-1.onrender.com/";
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
            sensor.setTaskID(null);
            sensor.setSchedule(false);
            sensor.setTimeSchedule(null);
            sensorRepository.save(sensor);

            String url =   urlFetch + "/api/turn-on/" + sensor.getStation().getId() +"/" + idSensor;
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

        }catch (Exception ex){
            return ex.getMessage();
        }
    }
    private ThreadPoolTaskScheduler taskScheduler;
    private Map<Integer, ScheduledFuture<?>> scheduledTasks;
    private int nextTaskId;

    @PostConstruct
    public void initialize() {
        taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.initialize();
        scheduledTasks = new ConcurrentHashMap<>();
        nextTaskId = 1;
    }

    public int scheduleTimeToInactive(LocalDateTime time, String sensorID) {
        Runnable task = () -> {
            System.out.println("OK");
            inActiveSensor(sensorID);
            Sensor s = sensorRepository.findSensorById(sensorID);
            s.setIsSchedule(false);
            s.setTaskID(null);
            s.setTimeSchedule(null);
            if(s!= null){
                sensorRepository.save(s);
            }

        };
        ZoneId zid = ZoneId.of("Asia/Saigon");
        Instant instant = time.atZone(zid).toInstant();
        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(task, instant);
        int taskId = nextTaskId++;
        scheduledTasks.put(taskId, scheduledFuture);
        Sensor sensor = sensorRepository.findSensorById(sensorID);
        if(sensor!= null){
            sensor.setTaskID(taskId);
            sensor.setTimeSchedule(time);
            sensor.setIsSchedule(true);
            sensorRepository.save(sensor);
        }

        return taskId;
    }

    public String cancelScheduledTask(int taskId, String idSensor ) {
        ScheduledFuture<?> future = scheduledTasks.get(taskId);
        if (future != null) {
            future.cancel(true);
            scheduledTasks.remove(taskId);
            Sensor s = sensorRepository.findSensorById(idSensor);
            if(s!= null && s.getTaskID().equals(taskId)){
                s.setTaskID(null);
                s.setIsSchedule(false);
                s.setTimeSchedule(null);
                sensorRepository.save(s);
            }

            System.out.println("Task " + taskId + " cancelled.");
            return "Task " + taskId + " cancelled.";

        } else {
            System.out.println("Task " + taskId + " not found or already cancelled.");
            return "Task " + taskId + " not found or already cancelled.";
        }
    }

    public String editScheduledTask(int taskId, LocalDateTime newTime, String sensorID) {
        ScheduledFuture<?> scheduledTask = scheduledTasks.get(taskId);
        if (scheduledTask != null) {
            // Cancel the existing task
            scheduledTask.cancel(true);

            // Schedule a new task with updated parameters
            ZoneId zid = ZoneId.of("Asia/Saigon");
            Instant newInstant = newTime.atZone(zid).toInstant();
            Runnable task = () -> {
                System.out.println("OK (edited)");
                inActiveSensor(sensorID);
                Sensor sensor = sensorRepository.findSensorById(sensorID);
                if (sensor != null) {
                    sensor.setIsSchedule(false);
                    sensor.setTaskID(null);
                    sensor.setTimeSchedule(null);
                    sensorRepository.save(sensor);
                }
            };

            ScheduledFuture<?> newScheduledFuture = taskScheduler.schedule(task, newInstant);
            scheduledTasks.put(taskId, newScheduledFuture);

            // Update sensor with new task ID and schedule status
            Sensor sensor = sensorRepository.findSensorById(sensorID);
            if (sensor != null) {
                sensor.setTaskID(taskId);
                sensor.setTimeSchedule(newTime);
                sensor.setIsSchedule(true);
                sensorRepository.save(sensor);
            }

            System.out.println("Task " + taskId + " edited.");
            return "Task " + taskId + " edited.";
        } else {
            System.out.println("Task " + taskId + " not found.");
            return "Task " + taskId + " not found.";
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
                Calendar cal = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                com.spring.iot.dto.Message m = new com.spring.iot.dto.Message("server", "client", "Update", dateFormat.format(cal.getTime()), Status.MESSAGE);
                simpMessagingTemplate.convertAndSendToUser(m.getReceiverName(), "/private", m);
                return "Success";
            }
            else
                return  "Failed";

        }catch (Exception ex){
            return ex.getMessage();
        }
    }
    public void checkToNotify(String idSensor, Double value, LocalDateTime time, List<SensorValue> listToCheck){
        String  typeSensor = idSensor.split("_")[0];
        String content = "";
        String state = "";
        switch (typeSensor){
            case "temp":
                Boolean isNotify = true;
                if(value>= 20  && value <22){

                    for (SensorValue sensorValue : listToCheck){
                        Double dataSensor= 0.0;
                        if(sensorValue.getSensor().getId().contains("Relay")) continue;
                        dataSensor = Double.parseDouble(sensorValue.getValue());
                        if(dataSensor >22)
                        {
                            isNotify = false;
                            break;
                        }
                    }
                    //low
                    content = "Nhiệt độ của máy cảm biến " + idSensor+" quá thấp";
                    state = "Low";
                }

                if(value >=36.5){
                    for (SensorValue sensorValue : listToCheck){
                        Double dataSensor= 0.0;
                        if(sensorValue.getSensor().getId().contains("Relay")) continue;
                        dataSensor = Double.parseDouble(sensorValue.getValue());
                        if(dataSensor <36.5)
                        {
                            isNotify = false;
                            break;
                        }
                    }
                    //very high
                    content = "Nhiệt độ của máy cảm biến " + idSensor+" quá cao";
                    state = "Very high";
                }
                break;
            case "ph":
                if(value>=5  && value <5.5){
                    for (SensorValue sensorValue : listToCheck){
                        Double dataSensor= 0.0;
                        if(sensorValue.getSensor().getId().contains("Relay")) continue;
                        dataSensor = Double.parseDouble(sensorValue.getValue());
                        if(dataSensor >5.5)
                        {
                            isNotify = false;
                            break;
                        }
                    }
                    //low
                    content = "Nổng độ PH của máy cảm biến " + idSensor+" quá thấp";
                    state = "Low";
                }

                if(value >=7.5){
                    for (SensorValue sensorValue : listToCheck){
                        Double dataSensor= 0.0;
                        if(sensorValue.getSensor().getId().contains("Relay")) continue;
                        dataSensor = Double.parseDouble(sensorValue.getValue());
                        if(dataSensor <7.5)
                        {
                            isNotify = false;
                            break;
                        }
                    }
                    //very high
                    content = "Nồng độ PH của máy cảm biến " + idSensor+" quá cao";
                    state = "Very high";
                }
                break;

            default:
                content  ="";
                break;

        }
        if (content != "") {
            Notification notification   = new Notification(0,content,state, value.toString(), time ,false);
            notificationRepository.saveAndFlush(notification);
        }
    }


}
