package org.philbour.weatherservice.model;

import org.philbour.weatherservice.model.dao.MetricValueDao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MetricValue {

    private final Long metricId;
    private final int metricValue; // https://stackoverflow.com/questions/2224503/how-to-map-an-entity-field-whose-name-is-a-reserved-word-in-jpa

    @JsonCreator
    public MetricValue(@JsonProperty("metricId") Long metricId, @JsonProperty("metricValue") int metricValue) {
        this.metricId = metricId;
        this.metricValue = metricValue;
    }

    public MetricValue(MetricValueDao metricValueDao) {
        this.metricId = metricValueDao.getMetric().getId();
        this.metricValue = metricValueDao.getMetricValue();
    }

    public Long getMetricId() {
        return metricId;
    }

    public int getMetricValue() {
        return metricValue;
    }

}
