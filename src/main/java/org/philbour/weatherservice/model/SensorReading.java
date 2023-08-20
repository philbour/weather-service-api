package org.philbour.weatherservice.model;

import org.philbour.weatherservice.model.resource.SensorReadingResource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SensorReading {

    private Long id;
    private final Long sensorId;
    private final LocalDateTime timeOfReading;
    private final List<MetricValue> metrics;

    @JsonCreator
    public SensorReading(@JsonProperty("id") Long id, @JsonProperty("sensorId") Long sensorId,
            @JsonProperty("timeOfReading") LocalDateTime timeOfReading,
            @JsonProperty("metrics") List<MetricValue> metrics) {
        this.id = id;
        this.sensorId = sensorId;
        this.timeOfReading = timeOfReading;
        this.metrics = new ArrayList<>(metrics);
    }

    public SensorReading(SensorReadingResource reading) {
        this.sensorId = reading.getSensorId();
        this.timeOfReading = reading.getTimeOfReading();
        this.metrics = new ArrayList<>(reading.getMetrics());
    }

    public SensorReading(Sensor sensor, LocalDateTime timeOfReading, List<MetricValue> metrics) {
        this.sensorId = sensor.getId();
        this.timeOfReading = timeOfReading;
        this.metrics = metrics;
    }

    public Long getId() {
        return id;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public LocalDateTime getTimeOfReading() {
        return timeOfReading;
    }

    public List<MetricValue> getMetrics() {
        return metrics;
    }

}
