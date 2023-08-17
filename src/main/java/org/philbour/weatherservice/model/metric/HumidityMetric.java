package org.philbour.weatherservice.model.metric;

import org.philbour.weatherservice.model.Metric;
import org.philbour.weatherservice.model.MetricType;

public class HumidityMetric implements Metric {

    @Override
    public MetricType getType() {
        return MetricType.HUMIDITY;
    }

    @Override
    public String getDescription() {
        return null;
    }

}
