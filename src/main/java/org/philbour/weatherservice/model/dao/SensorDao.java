package org.philbour.weatherservice.model.dao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sensor")
public class SensorDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String location;

    public SensorDao() {
    }

    public SensorDao(String location) {
        this.location = location;
    }

    @JsonCreator
    public SensorDao(@JsonProperty("id") Long id, @JsonProperty("location") String location) {
        this.id = id;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

}
