package com.spring.iot.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "station")
@Data
public class Station {
    @Id
    private String id;

    private String name;
    private Boolean active;


//    private Boolean isActive;


    public Station() {
    }

    public Station(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "station",fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Sensor> sensors = new HashSet<>();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    //    public Boolean getActive() {
//        return isActive;
//    }
//
//    public void setActive(Boolean active) {
//        isActive = active;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(Set<Sensor> sensors) {
        this.sensors = sensors;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}