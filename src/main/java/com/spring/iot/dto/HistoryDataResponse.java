package com.spring.iot.dto;

import com.spring.iot.entities.SensorValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryDataResponse {
    List<SensorValue> values1h;
    List<SensorValue> values1d;
    List<SensorValue> values1w;
    List<SensorValue> values1m;
}
