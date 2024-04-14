package com.spring.iot.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MinMaxResponse {
    private String max1h;
    private String max1d;
    private String max1w;
    private String max1m;
    private String min1h;
    private String min1d;
    private String min1w;
    private String min1m;


}
