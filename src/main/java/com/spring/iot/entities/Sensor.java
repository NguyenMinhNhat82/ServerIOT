package com.spring.iot.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sensor")
@Data
public class Sensor {
    @Id
    private String id;
    private Boolean active;



    @ManyToOne
    @JoinColumn(name = "id_station")
    private Station station;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sensor",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<SensorValue> sensorValues = new HashSet<>();



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public Sensor() {
    }

    public Sensor(String id, Station station) {
        this.id = id;
        this.station = station;
    }
}
