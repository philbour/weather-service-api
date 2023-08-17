package org.philbour.weatherservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MetricValue {

    private final String metricId;
    private final String value;

    @JsonCreator
    public MetricValue(@JsonProperty("metricId") String metricId, @JsonProperty("value") String value) {
        this.metricId = metricId;
        this.value = value;
    }

    public String getMetricId() {
        return metricId;
    }

    public String getValue() {
        return value;
    }

}
