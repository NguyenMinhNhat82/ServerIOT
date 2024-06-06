package com.spring.iot.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class MinMaxResponse {
    private String max1h;
    private String max1d;
    private String max1w;
    private String max1m;
    private String min1h;
    private String min1d;
    private String min1w;
    private String min1m;

    public MinMaxResponse() {
    }



    public String getMax1h() {
        return max1h;
    }

    public void setMax1h(String max1h) {
        this.max1h = max1h;
    }

    public String getMax1d() {
        return max1d;
    }

    public void setMax1d(String max1d) {
        this.max1d = max1d;
    }

    public String getMax1w() {
        return max1w;
    }

    public void setMax1w(String max1w) {
        this.max1w = max1w;
    }

    public String getMax1m() {
        return max1m;
    }

    public void setMax1m(String max1m) {
        this.max1m = max1m;
    }

    public String getMin1h() {
        return min1h;
    }

    public void setMin1h(String min1h) {
        this.min1h = min1h;
    }

    public String getMin1d() {
        return min1d;
    }

    public void setMin1d(String min1d) {
        this.min1d = min1d;
    }

    public String getMin1w() {
        return min1w;
    }

    public void setMin1w(String min1w) {
        this.min1w = min1w;
    }

    public String getMin1m() {
        return min1m;
    }

    public void setMin1m(String min1m) {
        this.min1m = min1m;
    }
}
