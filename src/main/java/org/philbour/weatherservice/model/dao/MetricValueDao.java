package org.philbour.weatherservice.model.dao;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "metric_value")
public class MetricValueDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_reading_id", referencedColumnName = "id")
    private SensorReadingDao sensorReading;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "metric_id")
    private MetricDao metric;

    private int metricValue;

    public MetricValueDao() {
    }

    /*
     * public MetricValueDao(MetricDao metric, int metricValue) { this.metric = metric; this.metricValue = metricValue;
     * } public MetricValueDao(SensorReadingDao sensorReading, MetricDao metric, int metricValue) { this.sensorReading =
     * sensorReading; this.metric = metric; this.metricValue = metricValue; } public MetricValueDao(Long id,
     * SensorReadingDao sensorReading, MetricDao metric, int metricValue) { this.id = id; this.sensorReading =
     * sensorReading; this.metric = metric; this.metricValue = metricValue; }
     */

    public MetricValueDao(MetricDao metric, SensorReadingDao sensorReading, int metricValue) {
        this.sensorReading = sensorReading;
        this.metric = metric;
        this.metricValue = metricValue;
    }

    public Long getId() {
        return id;
    }

    public SensorReadingDao getSensorReading() {
        return sensorReading;
    }

    public MetricDao getMetric() {
        return metric;
    }

    public int getMetricValue() {
        return metricValue;
    }

}
