package com.spring.iot.util;

import com.spring.iot.entities.Station;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static ArrayList<Station> Station = new ArrayList<>();


    public static Map<String, Station> historyValue = new HashMap<String, Station>() {{
//        for (Station s: Station) {
//            put(s.getName(),null);
//        }
        put("NBIOT 0002",null);
    }};
    public static Map<String,Float> MinCO = new HashMap<>(){{
        for (Station s: Station) {
            put(s.getName(),0f);
        }
    }};;
    public static Map<String, Float> MaxCO = new HashMap<>(){{
        put("station1", 0f);
        put("station2", 0f);
        put("station3", 0f);
        put("station4", 0f);
        put("station5", 0f);
    }};;
    public static Map<String,Float> MinNO = new HashMap<>(){{
        put("station1", 0f);
        put("station2", 0f);
        put("station3", 0f);
        put("station4", 0f);
        put("station5", 0f);
    }};;
    public static Map<String, Float> MaxNO = new HashMap<>(){{
        put("station1", 0f);
        put("station2", 0f);
        put("station3", 0f);
        put("station4", 0f);
        put("station5", 0f);
    }};;
    public static Map<String,Float> MinNO2 = new HashMap<>(){{
        put("station1", 0f);
        put("station2", 0f);
        put("station3", 0f);
        put("station4", 0f);
        put("station5", 0f);
    }};;
    public static Map<String, Float> MaxNO2 = new HashMap<>(){{
        put("station1", 0f);
        put("station2", 0f);
        put("station3", 0f);
        put("station4", 0f);
        put("station5", 0f);
    }};;
    public static Map<String,Float> MinO3 = new HashMap<>(){{
        put("station1", 0f);
        put("station2", 0f);
        put("station3", 0f);
        put("station4", 0f);
        put("station5", 0f);
    }};;
    public static Map<String, Float> MaxO3 = new HashMap<>(){{
        put("station1", 0f);
        put("station2", 0f);
        put("station3", 0f);
        put("station4", 0f);
        put("station5", 0f);
    }};;
    public static Map<String,Float> MinSO2 = new HashMap<>(){{
        put("station1", 0f);
        put("station2", 0f);
        put("station3", 0f);
        put("station4", 0f);
        put("station5", 0f);
    }};;
    public static Map<String, Float> MaxSO2 = new HashMap<>(){{
        put("station1", 0f);
        put("station2", 0f);
        put("station3", 0f);
        put("station4", 0f);
        put("station5", 0f);
    }};;
    public static Map<String,Float> MinPM25 = new HashMap<>(){{
        put("station1", 0f);
        put("station2", 0f);
        put("station3", 0f);
        put("station4", 0f);
        put("station5", 0f);
    }};;
    public static Map<String, Float> MaxPM25 = new HashMap<>(){{
        put("station1", 0f);
        put("station2", 0f);
        put("station3", 0f);
        put("station4", 0f);
        put("station5", 0f);
    }};;
    public static Map<String,Float> MinPM10 = new HashMap<>(){{
        put("station1", 0f);
        put("station2", 0f);
        put("station3", 0f);
        put("station4", 0f);
        put("station5", 0f);
    }};;
    public static Map<String, Float> MaxPM10 = new HashMap<>(){{
        put("station1", 0f);
        put("station2", 0f);
        put("station3", 0f);
        put("station4", 0f);
        put("station5", 0f);
    }};;
    public static Map<String,Float> MinNH3 = new HashMap<>(){{
        put("station1", 0f);
        put("station2", 0f);
        put("station3", 0f);
        put("station4", 0f);
        put("station5", 0f);
    }};;
    public static Map<String, Float> MaxNH3 = new HashMap<>(){{
        put("station1",0f);
        put("station2", 0f);
        put("station3", 0f);
        put("station4", 0f);
        put("station5", 0f);
    }};;
}
