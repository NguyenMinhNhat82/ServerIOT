package com.spring.iot.dto;

public class AverageValueResponse {
    private String standardDeviation1h;
    private String standardDeviation1d;
    private String standardDeviation1w;
    private String standardDeviation1m;

    private String average1h;
    private String average1d;
    private String average1w;
    private String average1m;

    public AverageValueResponse() {
    }

    public AverageValueResponse(String standardDeviation1h, String standardDeviation1d, String standardDeviation1w, String standardDeviation1m, String average1h, String average1d, String average1w, String average1m) {
        this.standardDeviation1h = standardDeviation1h;
        this.standardDeviation1d = standardDeviation1d;
        this.standardDeviation1w = standardDeviation1w;
        this.standardDeviation1m = standardDeviation1m;
        this.average1h = average1h;
        this.average1d = average1d;
        this.average1w = average1w;
        this.average1m = average1m;
    }

    public String getStandardDeviation1h() {
        return standardDeviation1h;
    }

    public void setStandardDeviation1h(String standardDeviation1h) {
        this.standardDeviation1h = standardDeviation1h;
    }

    public String getStandardDeviation1d() {
        return standardDeviation1d;
    }

    public void setStandardDeviation1d(String standardDeviation1d) {
        this.standardDeviation1d = standardDeviation1d;
    }

    public String getStandardDeviation1w() {
        return standardDeviation1w;
    }

    public void setStandardDeviation1w(String standardDeviation1w) {
        this.standardDeviation1w = standardDeviation1w;
    }

    public String getStandardDeviation1m() {
        return standardDeviation1m;
    }

    public void setStandardDeviation1m(String standardDeviation1m) {
        this.standardDeviation1m = standardDeviation1m;
    }

    public String getAverage1h() {
        return average1h;
    }

    public void setAverage1h(String average1h) {
        this.average1h = average1h;
    }

    public String getAverage1d() {
        return average1d;
    }

    public void setAverage1d(String average1d) {
        this.average1d = average1d;
    }

    public String getAverage1w() {
        return average1w;
    }

    public void setAverage1w(String average1w) {
        this.average1w = average1w;
    }

    public String getAverage1m() {
        return average1m;
    }

    public void setAverage1m(String average1m) {
        this.average1m = average1m;
    }
}
