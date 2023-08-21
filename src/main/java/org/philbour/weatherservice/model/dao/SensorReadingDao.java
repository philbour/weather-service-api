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
    private SensorDao sensor;

    private LocalDateTime timeOfReading;

    @OneToMany(mappedBy = "sensorReading", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private final List<MetricValueDao> metrics = new ArrayList<>();

    public SensorReadingDao() {
    }

    public SensorReadingDao(SensorDao sensor, LocalDateTime timeOfReading) {
        this.sensor = sensor;
        this.timeOfReading = timeOfReading;
    }

    public SensorReadingDao(Long id, SensorDao sensor, LocalDateTime timeOfReading) {
        this.id = id;
        this.sensor = sensor;
        this.timeOfReading = timeOfReading;
    }

    /*
     * public SensorReadingDao(SensorDao sensor, LocalDateTime timeOfReading, List<MetricValueDao> metrics) {
     * this.sensor = sensor; this.timeOfReading = timeOfReading; this.metrics.addAll(metrics); } public
     * SensorReadingDao(Long id, SensorDao sensor, LocalDateTime timeOfReading, List<MetricValueDao> metrics) { this.id
     * = id; this.sensor = sensor; this.timeOfReading = timeOfReading; this.metrics.addAll(metrics); }
     */

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

    /*
     * public void setId(Long id) { this.id = id; } public void setSensor(SensorDao sensor) { this.sensor = sensor; }
     * public void setTimeOfReading(LocalDateTime timeOfReading) { this.timeOfReading = timeOfReading; } public void
     * setMetrics(List<MetricValueDao> metrics) { this.metrics = metrics; }
     */

    public void addMetricValue(MetricValueDao metricValue) {
        metrics.add(metricValue);
    }

}
