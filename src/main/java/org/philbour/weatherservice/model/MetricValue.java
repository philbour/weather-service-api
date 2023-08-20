package org.philbour.weatherservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MetricValue {

    private final String metricId;
    private final int metricValue; // https://stackoverflow.com/questions/2224503/how-to-map-an-entity-field-whose-name-is-a-reserved-word-in-jpa

    @JsonCreator
    public MetricValue(@JsonProperty("metricId") String metricId, @JsonProperty("metricValue") int metricValue) {
        this.metricId = metricId;
        this.metricValue = metricValue;
    }

    public String getMetricId() {
        return metricId;
    }

    public int getMetricValue() {
        return metricValue;
    }

}
