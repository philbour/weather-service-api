package org.philbour.weatherservice.model.resource;

import org.philbour.weatherservice.model.MetricValue;

import com.fasterxml.jackson.annotation.JsonFormat;

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
    // @DateTimeFormat(iso = DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    // @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
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
