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
    private final Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_reading_id", referencedColumnName = "id")
    private final SensorReadingDao sensorReading;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "metric_id")
    private final MetricDao metric;

    private final int metricValue;

    public MetricValueDao(Long id, SensorReadingDao sensorReading, MetricDao metric, int metricValue) {
        this.id = id;
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
