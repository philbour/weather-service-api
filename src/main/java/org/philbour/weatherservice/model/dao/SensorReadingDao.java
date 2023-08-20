package org.philbour.weatherservice.model.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "sensor_reading")
public class SensorReadingDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_id")
    private final SensorDao sensor;

    private final LocalDateTime timeOfReading;

    @OneToMany(mappedBy = "sensorReading", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<MetricValueDao> metrics = new ArrayList<>();

    public SensorReadingDao(SensorDao sensor, LocalDateTime timeOfReading) {
        this.sensor = sensor;
        this.timeOfReading = timeOfReading;
    }

    public SensorReadingDao(Long id, SensorDao sensor, LocalDateTime timeOfReading) {
        this.id = id;
        this.sensor = sensor;
        this.timeOfReading = timeOfReading;
    }

    public Long getId() {
        return id;
    }

    public SensorDao getSensor() {
        return sensor;
    }

    public LocalDateTime getTimeOfReading() {
        return timeOfReading;
    }

    public List<MetricValueDao> getMetrics() {
        return metrics;
    }

}
