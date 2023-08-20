package org.philbour.weatherservice.model.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class MetricResource {

    @NotEmpty
    private final String metricType;

    public MetricResource(@JsonProperty("metricType") String metricType) {
        this.metricType = metricType;
    }

    public String getMetricType() {
        return metricType;
    }

}
