package org.philbour.weatherservice.model;

public enum MetricType {

    HUMIDITY("humidity", "the concentration of water vapor present in the air", "percent"),
    PRESSURE("pressure", "", "mbar"),
    TEMPERATURE("temperature", "", "c"),
    WIND_SPEED("windspeed", "", "kph");

    private final String name;
    private final String description;
    private final String unit;

    MetricType(String name, String description, String unit) {
        this.name = name;
        this.description = description;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUnit() {
        return unit;
    }
}
