package org.philbour.weatherservice.model.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class SensorResource {

    @NotNull
    private final String location;

    public SensorResource(@JsonProperty("location") String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

}
