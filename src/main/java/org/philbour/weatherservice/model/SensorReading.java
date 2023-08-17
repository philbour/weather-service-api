package org.philbour.weatherservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SensorReading {

    private final String id;
    private final String sensorId;
    private final LocalDateTime timeOfReading;
    private final List<MetricValue> metrics;

    @JsonCreator
    public SensorReading(@JsonProperty("id") String id, @JsonProperty("sensorId") String sensorId,
            @JsonProperty("timeOfReading") LocalDateTime timeOfReading,
            @JsonProperty("metrics") List<MetricValue> metrics) {
        this.id = id;
        this.sensorId = sensorId;
        this.timeOfReading = timeOfReading;
        this.metrics = new ArrayList<>(metrics);
    }

    public String getId() {
        return id;
    }

    public String getSensorId() {
        return sensorId;
    }

    public LocalDateTime getTimeOfReading() {
        return timeOfReading;
    }

    public List<MetricValue> getMetrics() {
        return metrics;
    }

}
