package org.philbour.weatherservice.model;

import org.philbour.weatherservice.model.dao.SensorDao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Sensor {

    private Long id;
    private String location;

    public Sensor() {
    }

    public Sensor(String location) {
        this.location = location;
    }

    @JsonCreator
    public Sensor(@JsonProperty("id") Long id, @JsonProperty("location") String location) {
        this.id = id;
        this.location = location;
    }

    public Sensor(SensorDao sensorDao) {
        this.id = sensorDao.getId();
        this.location = sensorDao.getLocation();
    }

    public Long getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

}
