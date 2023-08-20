package org.philbour.weatherservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Metric {

    private Long id;
    private String metricType;

    public Metric() {
    }

    public Metric(String metricType) {
        this.metricType = metricType;
    }

    @JsonCreator
    public Metric(@JsonProperty("id") Long id, @JsonProperty("metricType") String metricType) {
        this.id = id;
        this.metricType = metricType;
    }

    public Long getId() {
        return id;
    }

    public String getMetricType() {
        return metricType;
    }

}
