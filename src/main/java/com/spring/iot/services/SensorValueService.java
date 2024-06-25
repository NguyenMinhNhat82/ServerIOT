package com.spring.iot.services;

import com.spring.iot.dto.*;
import com.spring.iot.entities.Sensor;
import com.spring.iot.entities.SensorValue;
import com.spring.iot.entities.Station;
import com.spring.iot.repositories.SensorRepository;
import com.spring.iot.repositories.SensorValueRepository;
import com.spring.iot.repositories.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
public class SensorValueService {
    @Autowired
    private SensorValueRepository sensorValueRepository;
    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private StationRepository stationRepository;

    public SensorValue addOrUpdate(SensorValue sensorValue) {
        List<SensorValue> sensorValueList = sensorValueRepository.getListValueBySensor(sensorValue.getSensor().getId());
        if (sensorValueList.size() == 0)
            return sensorValueRepository.save(sensorValue);
        LocalDateTime fromDate = sensorValueList.get(0).getTimeUpdate();
        LocalDateTime toDate = sensorValue.getTimeUpdate();
        Duration duration = Duration.between(fromDate, toDate);
        if (duration.getSeconds() > 3600 * 24 * 31) {
            sensorValueRepository.delete(sensorValueList.get(0));
        }
        return sensorValueRepository.saveAndFlush(sensorValue);
    }

    public CurrentResponse currentData(String idStation) {
        List<Sensor> listSensor = sensorRepository.getSensorByStation_Id(idStation);
        List<CurrentResponse.ValueOfSensor> listTemp = new ArrayList<>();
        List<CurrentResponse.ValueOfSensor> listHumi = new ArrayList<>();
        List<CurrentResponse.ValueOfSensor> listPH = new ArrayList<>();
        List<CurrentResponse.ValueOfSensor> listEc = new ArrayList<>();
        List<CurrentResponse.ValueOfSensor> listKali = new ArrayList<>();
        List<CurrentResponse.ValueOfSensor> listPhotpho = new ArrayList<>();
        List<CurrentResponse.ValueOfSensor> listNito = new ArrayList<>();
        List<CurrentResponse.RelayValue> listRelay = new ArrayList<>();

        for (Sensor s : listSensor) {
            SensorValue lastest = sensorValueRepository.getListValueBySensorCurrent(s.getId()).get(0);
            SensorValue old = sensorValueRepository.getListValueBySensorCurrent(s.getId()).get(1);
            String typeSensor = s.getId().split("_")[0];
            if (s.getId().split("_")[0].equals("Relay")){
                CurrentResponse.RelayValue relayValue = new CurrentResponse.RelayValue(s.getId() , lastest.getValue());
                listRelay.add(relayValue);
            }
            else {
                Double changeValue = Double.parseDouble(lastest.getValue()) - Double.parseDouble(old.getValue());
                CurrentResponse.ValueOfSensor value = new CurrentResponse.ValueOfSensor();
                value.setChangeValue(String.valueOf(Math.abs(changeValue)));
                value.setState(changeValue < 0 ? -1 : (changeValue == 0 ? 0 : 1));
                value.setValue(lastest.getValue());
                value.setName(s.getId());
                value.setActive(s.getActive());
                switch (s.getId().split("_")[0]) {
                    case "temp":
                        listTemp.add(value);
                        break;
                    case "humi":
                        listHumi.add(value);
                        break;
                    case "ph":
                        listPH.add(value);
                        break;
                    case "EC":
                        listEc.add(value);
                        break;
                    case "Kali":
                        listKali.add(value);
                        break;
                    case "Photpho":
                        listPhotpho.add(value);
                        break;
                    case "Nito":
                        listNito.add(value);
                        break;
                }
            }

        }
        return new CurrentResponse(listTemp,listHumi,listPH,listEc,listKali,listNito,listPhotpho,listRelay);
    }

    public List<SensorValue> listValue(String idSensor) {
        return sensorValueRepository.getListValueBySensor(idSensor);
    }

    public List<SensorValue> CurrentDataSensor(String station) {
        List<SensorValue> list = new ArrayList<>();
        List<Sensor> s = sensorRepository.getSensorByStation_Id(station);
        for (Sensor v : s) {
            list.addAll(sensorValueRepository.findFirstBySensor_IdOrderByTimeUpdateDesc(v.getId()));
        }
        return list;
    }

    public List<SensorValue> sensorValueList() {
        return sensorValueRepository.findAll();
    }

    public List<SensorValue> getCurrentListOfRelay(String station, String value) {
        List<SensorValue> arr = new ArrayList<>();
        for (SensorValue s : this.CurrentDataSensor(station)) {
            if (s.getSensor().getId().split("_")[0].equals(value)) {
                arr.add(s);
            }
        }
        return arr;
    }
    public List<Map<String, Object>> getDataByMonthGroupByWeek(int year, int month) {
        return sensorValueRepository.getDataByMonthGroupByWeek(year, month);
    }

    public List<SensorValue> DataSensorHour(String idsensor) {
        List<SensorValue> newlist = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now();
        List<SensorValue> value = sensorValueRepository.getSensorValuesBySensor_Id(idsensor);
        for (SensorValue s : value) {
            LocalDateTime timeUpdate = s.getTimeUpdate();
            long secondsDifference = ChronoUnit.SECONDS.between(timeUpdate, currentTime);
            if (secondsDifference <= 3600) {
                newlist.add(s);
            }
        }
        return newlist;
    }

    public List<SensorValue> DataSensorDay(String idsensor) {
        List<SensorValue> newlist = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now();
        List<SensorValue> value = sensorValueRepository.getSensorValuesBySensor_Id(idsensor);
        for (SensorValue s : value) {
            LocalDateTime timeUpdate = s.getTimeUpdate();
            long secondsDifference = ChronoUnit.SECONDS.between(timeUpdate, currentTime);
            if (secondsDifference <= 86400) {
                newlist.add(s);
            }
        }
        return newlist;
    }


    public List<SensorValue> DataAllSensorInDay() {
        List<SensorValue> newlist = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now();
        List<SensorValue> value = sensorValueRepository.findAll();
        for (SensorValue s : value) {
            LocalDateTime timeUpdate = s.getTimeUpdate();
            long secondsDifference = ChronoUnit.SECONDS.between(timeUpdate, currentTime);
            if (secondsDifference <= 86400) {
                newlist.add(s);
            }
        }
        return newlist;
    }

    public List<SensorValue> DataSensorWeek(String idsensor) {
        List<SensorValue> newlist = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now();
        List<SensorValue> value = sensorValueRepository.getSensorValuesBySensor_Id(idsensor);
        for (SensorValue s : value) {
            LocalDateTime timeUpdate = s.getTimeUpdate();
            long secondsDifference = ChronoUnit.SECONDS.between(timeUpdate, currentTime);
            if (secondsDifference <= 604800) {
                newlist.add(s);
            }
        }
        return newlist;
    }

    public List<SensorValue> DataSensorMonth(String idsensor) {
        List<SensorValue> newlist = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now();
        List<SensorValue> value = sensorValueRepository.getSensorValuesBySensor_Id(idsensor);
        for (SensorValue s : value) {
            LocalDateTime timeUpdate = s.getTimeUpdate();
            long secondsDifference = ChronoUnit.SECONDS.between(timeUpdate, currentTime);
            if (secondsDifference <= 2592000) {
                newlist.add(s);
            }
        }
        return newlist;
    }

    public String MaxSensorHour(String idsensor) {
        Double max = 0.0;
        SensorValue value = new SensorValue();
        for (SensorValue s : DataSensorHour(idsensor)) {
            if (Double.parseDouble(s.getValue()) > max) {
                max = Double.parseDouble(s.getValue());
            }
        }

        return max.toString();
    }

    public String MaxSensorDay(String idsensor) {
        Double max = 0.0;
        SensorValue value = new SensorValue();
        for (SensorValue s : DataSensorDay(idsensor)) {
            if (Double.parseDouble(s.getValue()) > max) {
                max = Double.parseDouble(s.getValue());
            }
        }

        return max.toString();
    }

    public String MaxSensorWeek(String idsensor) {
        Double max = 0.0;
        SensorValue value = new SensorValue();
        for (SensorValue s : DataSensorWeek(idsensor)) {
            if (Double.parseDouble(s.getValue()) > max) {
                max = Double.parseDouble(s.getValue());
            }
        }

        return max.toString();
    }

    public static List<WeekRange> getAllWeeksInMonth(int year, int month) {
        List<WeekRange> weeks = new ArrayList<>();

        // Create YearMonth object for the specified year and month
        YearMonth yearMonth = YearMonth.of(year, month);

        // Get the first day of the month
        LocalDate firstDayOfMonth = yearMonth.atDay(1);

        // Get the last day of the month
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

        // Initialize start date for the first week
        LocalDate startDate = firstDayOfMonth;

        // Iterate through the weeks of the month
        while (startDate.isBefore(lastDayOfMonth) || startDate.isEqual(lastDayOfMonth)) {
            // Calculate end date of the current week
            LocalDate endDate = startDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

            // Adjust end date to the last day of the month if necessary
            if (endDate.isAfter(lastDayOfMonth)) {
                endDate = lastDayOfMonth;
            }

            // Add the week range to the list
            weeks.add(new WeekRange(startDate, endDate));

            // Move to the next week
            startDate = endDate.plusDays(1);
        }

        return weeks;
    }

    public MinMaxSensorByWeek.ValueDay getMinMaxOfSensorByWeek(String dateStart, String dateEnd, Sensor sensor, String week){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        // Parse the string to LocalDateTime
        LocalDateTime localDateTimeStart = LocalDateTime.parse(dateStart + "T00:00:00", formatter);
        LocalDateTime localDateTimeEnd = LocalDateTime.parse(dateEnd + "T23:59:59", formatter);
        List<SensorValue> list = sensorValueRepository.findBySensorAndTimeUpdateBetween( sensor,localDateTimeStart, localDateTimeEnd);
        SensorValue valueMax  =  list.size() !=0?list.get(0):null;
        SensorValue valueMin  =  list.size() !=0?list.get(0):null; ;
        for(SensorValue s: list){
            if(!s.getSensor().getId().contains("Relay")){
                if(Double.parseDouble(s.getValue()) < Double.parseDouble(valueMin.getValue())){
                    valueMin = s;
                }
                if(Double.parseDouble(s.getValue()) > Double.parseDouble(valueMax.getValue())){
                    valueMax = s;
                }
            }
        }
        MinMaxSensorByWeek.ValueDay valueHour = new MinMaxSensorByWeek.ValueDay();
        valueHour.setMin(valueMin == null? "0" :  valueMin.getValue());
        valueHour.setMax(valueMax == null? "0" :valueMax.getValue());
        valueHour.setMinAt(valueMin == null?null: valueMin.getTimeUpdate());
        valueHour.setMaxAt(valueMax == null?null:valueMax.getTimeUpdate());
        valueHour.setWeek(week.toString());

        return valueHour;



    }

    public MinMaxSensorByMonth.ValueWeek getMinMaxOfSensorByMonth(String dateStart, String dateEnd, Sensor sensor, Integer week){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        // Parse the string to LocalDateTime
        LocalDateTime localDateTimeStart = LocalDateTime.parse(dateStart + "T00:00:00", formatter);
        LocalDateTime localDateTimeEnd = LocalDateTime.parse(dateEnd + "T23:59:59", formatter);
        List<SensorValue> list = sensorValueRepository.findBySensorAndTimeUpdateBetween( sensor,localDateTimeStart, localDateTimeEnd);
        SensorValue valueMax  =  list.size() !=0?list.get(0):null;
        SensorValue valueMin  =  list.size() !=0?list.get(0):null; ;
        for(SensorValue s: list){
            if(!s.getSensor().getId().contains("Relay")){
                if(Double.parseDouble(s.getValue()) < Double.parseDouble(valueMin.getValue())){
                    valueMin = s;
                }
                if(Double.parseDouble(s.getValue()) > Double.parseDouble(valueMax.getValue())){
                    valueMax = s;
                }
            }
        }
        MinMaxSensorByMonth.ValueWeek valueHour = new MinMaxSensorByMonth.ValueWeek();
        valueHour.setMin(valueMin == null? "0" :  valueMin.getValue());
        valueHour.setMax(valueMax == null? "0" :valueMax.getValue());
        valueHour.setMinAt(valueMin == null?null: valueMin.getTimeUpdate());
        valueHour.setMaxAt(valueMax == null?null:valueMax.getTimeUpdate());
        valueHour.setWeek(week.toString());

        return valueHour;



    }
    public MinMaxSensorByMonth getMinAndMaxValueInAllWeekByMonth(int month, int year, String idStation){
        List<WeekRange> weekRanges = this.getAllWeeksInMonth(month,year);
        MinMaxSensorByMonth minMaxSensorByMonth = new MinMaxSensorByMonth();
        List<Sensor> sensors = sensorRepository.getSensorByStation_Id(idStation);
        List<MinMaxSensorByMonth.SensorMinMax> res = new ArrayList<>();
            for(Sensor s:  sensors){
                MinMaxSensorByMonth.SensorMinMax sensorMinMax = new MinMaxSensorByMonth.SensorMinMax();
                List<MinMaxSensorByMonth.ValueWeek> data = new ArrayList<>();
                sensorMinMax.setSensor(s.getId());
                int indexWeek = 0;
                for (WeekRange week : weekRanges) {
                    MinMaxSensorByMonth.ValueWeek element = getMinMaxOfSensorByMonth(week.getStartDate().toString(), week.getEndDate().toString()
                    ,s, ++indexWeek);
                    data.add(element);
                }
                sensorMinMax.setData(data);
                res.add(sensorMinMax);

            }
            minMaxSensorByMonth.setSensorMinMaxes(res);
            minMaxSensorByMonth.setNumWeek(weekRanges.size());
            return  minMaxSensorByMonth;
    }

    public MinMaxSensorByWeek getMinAndMaxValueInAllWeekByWeek(int month, int year, String idStation,int indexWeekChooose){
        WeekRange weekRanges = this.getAllWeeksInMonth(month,year).get(indexWeekChooose);
        MinMaxSensorByWeek minMaxSensorByMonth = new MinMaxSensorByWeek();
        List<Sensor> sensors = sensorRepository.getSensorByStation_Id(idStation);
        List<MinMaxSensorByWeek.SensorMinMax> res = new ArrayList<>();
        List<String> dayOfWeekDefault  = new ArrayList<>(Arrays.asList("Thứ 2","Thứ 3", "Thứ 4","Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật"));
        int dayStart =  weekRanges.startDate.getDayOfMonth();
        int dayEnd =  weekRanges.endDate.getDayOfMonth();
        int duration  = dayEnd - dayStart;
        for(Sensor s:  sensors){
            MinMaxSensorByWeek.SensorMinMax sensorMinMax = new MinMaxSensorByWeek.SensorMinMax();
            List<MinMaxSensorByWeek.ValueDay> data = new ArrayList<>();
            sensorMinMax.setSensor(s.getId());



            for(int i = 0; i <=duration ; i++ ){
                LocalDate d = weekRanges.getEndDate().minusDays(i);
                String dayOfWeek = dayOfWeekDefault.get(dayOfWeekDefault.size()-1 - i);
                MinMaxSensorByWeek.ValueDay element = getMinMaxOfSensorByWeek(d.toString(), d.toString()
                        ,s,dayOfWeek);
                data.add(element);

            }


            sensorMinMax.setData(data);
            res.add(sensorMinMax);

        }
        minMaxSensorByMonth.setSensorMinMaxes(res);
        minMaxSensorByMonth.setNumberIndex(duration+1);
        return  minMaxSensorByMonth;
    }

    public static class WeekRange {
        private final LocalDate startDate;
        private final LocalDate endDate;

        public WeekRange(LocalDate startDate, LocalDate endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }
    }

    public String MaxSensorMonth(String idsensor) {
        Double max = 0.0;
        SensorValue value = new SensorValue();
        for (SensorValue s : DataSensorMonth(idsensor)) {
            if (Double.parseDouble(s.getValue()) > max) {
                max = Double.parseDouble(s.getValue());
            }
        }

        return max.toString();
    }

    public String MinSensorHour(String idsensor) {
        Double min = 9999999.0;
        SensorValue value = new SensorValue();
        for (SensorValue s : DataSensorHour(idsensor)) {
            if (Double.parseDouble(s.getValue()) < min) {
                min = Double.parseDouble(s.getValue());
            }
        }

        return min.toString();
    }

    public String MinSensorDay(String idsensor) {
        Double min = 9999999.0;
        SensorValue value = new SensorValue();
        for (SensorValue s : DataSensorDay(idsensor)) {
            if (Double.parseDouble(s.getValue()) < min) {
                min = Double.parseDouble(s.getValue());
            }
        }

        return min.toString();
    }

    public String MinSensorWeek(String idsensor) {
        Double min = 9999999.0;
        SensorValue value = new SensorValue();
        for (SensorValue s : DataSensorWeek(idsensor)) {
            if (Double.parseDouble(s.getValue()) < min) {
                min = Double.parseDouble(s.getValue());
            }
        }

        return min.toString();
    }

    public String MinSensorMonth(String idsensor) {
        Double min = 9999999.0;
        SensorValue value = new SensorValue();
        for (SensorValue s : DataSensorMonth(idsensor)) {
            if (Double.parseDouble(s.getValue()) < min) {
                min = Double.parseDouble(s.getValue());
            }
        }

        return min.toString();
    }

    public MinMaxResponse minMaxResponse(String idSensor) {
        MinMaxResponse minMax = new MinMaxResponse();
        minMax.setMax1h(this.MaxSensorHour(idSensor));
        minMax.setMax1d(this.MaxSensorDay(idSensor));
        minMax.setMax1w(this.MaxSensorWeek(idSensor));
        minMax.setMax1m(this.MaxSensorMonth(idSensor));
        minMax.setMin1h(this.MinSensorHour(idSensor));
        minMax.setMin1d(this.MinSensorDay(idSensor));
        minMax.setMin1w(this.MinSensorWeek(idSensor));
        minMax.setMin1m(this.MinSensorMonth(idSensor));
        return minMax;
    }

    Double toDouble(String s) {
        return Double.parseDouble(s);
    }

    public MinMaxAllSensorResponse getMinMaxOfSenSortInStation(String date, String station) throws Exception {
        Station station1 = stationRepository.findStationById(station);
        String year = date.split("-")[0];
        String month = date.split("-")[1];
        String day = date.split("-")[2];
        if (station1 == null)
            throw new RuntimeException("Cant find station");
        List<Sensor> sensors = sensorRepository.getSensorByStation_Id(station);
        List<MinMaxAllSensorResponse.SensorMinMax> sensorMinMaxes = new ArrayList<>();
        for (Sensor s : sensors) {

            if (!s.getId().contains("Relay")) {
                List<MinMaxAllSensorResponse.ValueHour> valueHours = new ArrayList<>();
                for (int i = 0; i <= 23; i++) {
                    List<SensorValue> sensorValueList = sensorValueRepository.findAllSensorValueByDate(day, month, year, station, s.getId(), String.valueOf(i));
                    if (sensorValueList.size() != 0) {
                        SensorValue minSensor = sensorValueList.stream()
                                .min((a, b) -> toDouble(a.getValue()).compareTo(toDouble(b.getValue()))).orElseThrow(() -> new RuntimeException("error"));
                        SensorValue maxSensor = sensorValueList.stream()
                                .max((a, b) -> toDouble(a.getValue()).compareTo(toDouble(b.getValue()))).orElseThrow(() -> new RuntimeException("error"));
                        ;

                        MinMaxAllSensorResponse.ValueHour sensorMinMax = new MinMaxAllSensorResponse.ValueHour(String.valueOf(i), minSensor.getValue(), maxSensor.getValue(), maxSensor.getTimeUpdate(), minSensor.getTimeUpdate());
                        valueHours.add(sensorMinMax);
                    } else valueHours.add(new MinMaxAllSensorResponse.ValueHour(String.valueOf(i), "", "", null, null));
                }
                MinMaxAllSensorResponse.SensorMinMax minMax = new MinMaxAllSensorResponse.SensorMinMax(s.getId(), valueHours);
                sensorMinMaxes.add(minMax);
            }
        }
        return new MinMaxAllSensorResponse(sensorMinMaxes);
    }
    public String AverageHour (String idsensor)
    {
        if(idsensor.contains("Relay"))
            return "0";
        Double average = 0.0;
        Double sum = 0.0;
        int count = 0;
        SensorValue value = new SensorValue();
        for (SensorValue s : DataSensorHour(idsensor)) {
            count ++;
            sum += Double.parseDouble(s.getValue());
        }
        average = (double)Math.round((sum/count)*100)/100;
        return average.toString();
    }
    public String AverageDay (String idsensor)
    {
        if(idsensor.contains("Relay"))
            return "0";
        Double average = 0.0;
        Double sum = 0.0;
        int count = 0;
        SensorValue value = new SensorValue();
        for (SensorValue s : DataSensorDay(idsensor)) {
            count ++;
            sum += Double.parseDouble(s.getValue());
        }
        average = (double)Math.round((sum/count)*100)/100;
        return average.toString();
    }
    public String AverageWeek (String idsensor)
    {
        if(idsensor.contains("Relay"))
            return "0";
        Double average = 0.0;
        Double sum = 0.0;
        int count = 0;
        SensorValue value = new SensorValue();
        for (SensorValue s : DataSensorWeek(idsensor)) {
            count ++;
            sum += Double.parseDouble(s.getValue());
        }
        average = (double)Math.round((sum/count)*100)/100;
        return average.toString();
    }
    public String AverageMonth (String idsensor)
    {
        if(idsensor.contains("Relay"))
            return "0";
        Double average = 0.0;
        Double sum = 0.0;
        int count = 0;
        SensorValue value = new SensorValue();
        for (SensorValue s : DataSensorMonth(idsensor)) {
            count ++;
            sum += Double.parseDouble(s.getValue());
        }
        average = (double)Math.round((sum/count)*100)/100;
        return average.toString();
    }

    public String standardDeviation1h(String idSensor){
        List<SensorValue> data   = DataSensorHour(idSensor);
        Double average = 0.0;
        int count = 0 ;
        Double sum = 0.0;
        if(idSensor.contains("Relay"))
            return "0";
        if(data != null){
            count = data.size() -1;
            for(int i =0 ; i < data.size() -1 ; i ++){
                int nextValue = i +1 ;
                sum += Math.abs(Double.parseDouble(data.get(i).getValue())  - Double.parseDouble(data.get(nextValue).getValue())) ;

            }
            average = (double)Math.round((sum/count)*100)/100;
        }
        return average.toString();
    }

    public String standardDeviation1d(String idSensor){
        List<SensorValue> data   = DataSensorDay(idSensor);
        Double average = 0.0;
        int count = 0 ;
        Double sum = 0.0;
        if(idSensor.contains("Relay"))
            return "0";
        if(data != null){
            count = data.size() -1;
            for(int i =0 ; i < data.size() -1 ; i ++){
                int nextValue = i +1 ;
                sum += Math.abs(Double.parseDouble(data.get(i).getValue())  - Double.parseDouble(data.get(nextValue).getValue())) ;

            }
            average = (double)Math.round((sum/count)*100)/100;
        }
        return average.toString();
    }
    public String standardDeviation1w(String idSensor){
        List<SensorValue> data = DataSensorWeek(idSensor);
        Double average = 0.0;
        int count = 0 ;
        Double sum = 0.0;
        if(idSensor.contains("Relay"))
            return "0";
        if(data != null){
            count = data.size() -1;
            for(int i =0 ; i < data.size() -1 ; i ++){
                int nextValue = i +1 ;
                sum += Math.abs(Double.parseDouble(data.get(i).getValue())  - Double.parseDouble(data.get(nextValue).getValue())) ;

            }
            average = (double)Math.round((sum/count)*100)/100;
        }
        return average.toString();
    }
    public String standardDeviation1m(String idSensor){
        List<SensorValue> data = DataSensorMonth(idSensor);
        Double average = 0.0;
        int count = 0 ;
        Double sum = 0.0;
        if(idSensor.contains("Relay"))
            return "0";
        if(data != null){
            count = data.size() -1;
            for(int i =0 ; i < data.size() -1 ; i ++){
                int nextValue = i +1 ;
                sum += Math.abs(Double.parseDouble(data.get(i).getValue())  - Double.parseDouble(data.get(nextValue).getValue())) ;

            }
            average = (double)Math.round((sum/count)*100)/100;
        }
        return average.toString();
    }

    public AverageValueResponse averageValueResponse(String idSensor){
        return  new AverageValueResponse(standardDeviation1h(idSensor),
                standardDeviation1d(idSensor),
                standardDeviation1w(idSensor),
                standardDeviation1m(idSensor),
                AverageHour(idSensor),
                AverageDay(idSensor),
                AverageWeek(idSensor),
                AverageMonth(idSensor));
    }


    public HistoryDataResponse getAllHistoryDataOfSensor(String idSensor){
        List<SensorValue> values1h = sensorValueRepository.getSensorValueByBetweenTime(idSensor, 3600);
        List<SensorValue> values1d = sensorValueRepository.getSensorValueByBetweenTime(idSensor, 86400);
        List<SensorValue> values1w = sensorValueRepository.getSensorValueByBetweenTime(idSensor, 604800);
        List<SensorValue> values1m = sensorValueRepository.getSensorValueByBetweenTime(idSensor, 2678400);
        return  new HistoryDataResponse(values1h,values1d,values1w,values1m);
    }

    public List<SensorValue> findLatestSensorValues(Sensor sensor) {
        // Calculate the start time for the last 2 hours from now
        LocalDateTime startTime = LocalDateTime.now().minusHours(2);

        // Call the repository method to fetch SensorValues
        return sensorValueRepository.findLatestBySensorAndTimeUpdate(sensor, startTime);
    }


}
