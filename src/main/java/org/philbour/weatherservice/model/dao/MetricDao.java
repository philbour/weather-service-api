package org.philbour.weatherservice.model.dao;

import org.philbour.weatherservice.model.Metric;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "metric")
public class MetricDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String metricType;

    public MetricDao() {
    }

    public MetricDao(String metricType) {
        this.metricType = metricType;
    }

    public MetricDao(Long id, String metricType) {
        this.id = id;
        this.metricType = metricType;
    }

    public MetricDao(Metric metric) {
        this.id = metric.getId();
        this.metricType = metric.getMetricType();
    }

    public Long getId() {
        return id;
    }

    public String getMetricType() {
        return metricType;
    }

}
