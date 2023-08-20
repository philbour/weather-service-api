package org.philbour.weatherservice.model.resource;

import org.philbour.weatherservice.model.MetricValue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

public class SensorReadingResource {

    @NotNull
    private final Long sensorId;
    @NotNull
    @PastOrPresent(message = "Date cannot be in the future")
    private final LocalDateTime timeOfReading;
    @NotEmpty
    private final List<MetricValue> metrics;

    public SensorReadingResource(Long sensorId, LocalDateTime timeOfReading, List<MetricValue> metrics) {
        this.sensorId = sensorId;
        this.timeOfReading = timeOfReading;
        this.metrics = new ArrayList<>(metrics);
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
