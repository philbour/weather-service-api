package org.philbour.weatherservice.service;

import org.apache.commons.collections4.CollectionUtils;
import org.philbour.weatherservice.model.MetricValue;
import org.philbour.weatherservice.model.SensorReading;
import org.philbour.weatherservice.model.dao.MetricDao;
import org.philbour.weatherservice.model.dao.MetricValueDao;
import org.philbour.weatherservice.model.dao.SensorDao;
import org.philbour.weatherservice.model.dao.SensorReadingDao;
import org.philbour.weatherservice.model.resource.SensorReadingResource;
import org.philbour.weatherservice.repository.MetricRepository;
import org.philbour.weatherservice.repository.SensorReadingRepository;
import org.philbour.weatherservice.repository.SensorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SensorReadingService {

    private static final Logger LOG = LoggerFactory.getLogger(SensorReadingService.class);

    private final SensorReadingRepository sensorReadingRepository;
    private final SensorRepository sensorRepository;
    private final MetricRepository metricRepository;

    public SensorReadingService(SensorReadingRepository sensorReadingRepository, SensorRepository sensorRepository,
            MetricRepository metricRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
        this.sensorRepository = sensorRepository;
        this.metricRepository = metricRepository;
    }

    public SensorReading register(SensorReadingResource reading) {
        LOG.debug("Saving new sensor reading");
        SensorDao sensor = sensorRepository.getReferenceById(reading.getSensorId());

        SensorReadingDao readingToSave = new SensorReadingDao(sensor, reading.getTimeOfReading());

        List<MetricValueDao> metricValuesToSave = convert(reading.getMetrics(), readingToSave);
        metricValuesToSave.forEach(readingToSave::addMetricValue);

        SensorReadingDao sensorReadingDao = sensorReadingRepository.save(readingToSave);

        return new SensorReading(sensorReadingDao.getId(), sensorReadingDao.getSensor().getId(), sensorReadingDao
                .getTimeOfReading(), sensorReadingDao.getMetrics()
                        .stream()
                        .map(m -> new MetricValue(m.getMetric().getId(), m.getMetricValue()))
                        .collect(Collectors.toList()));
    }

    public List<SensorReading> getAll() {
        LOG.debug("Getting all sensor reading");
        List<SensorReadingDao> readings = sensorReadingRepository.findAll();

        if (CollectionUtils.isNotEmpty(readings)) {
            return readings.stream().map(SensorReading::new).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    public Optional<SensorReading> getById(Long id) {
        LOG.debug("Getting sensor reading {}", id);
        Optional<SensorReadingDao> optional = sensorReadingRepository.findById(id);
        return optional.isPresent() ? Optional.of(new SensorReading(optional.get())) : Optional.empty();
    }

    public int getMetricAverageBySensorId(Long sensorId, String metricType, LocalDate from, LocalDate to) {
        return sensorReadingRepository.getAverageForMetric(sensorId, metricType, from.atStartOfDay(), to.atTime(
                LocalTime.MAX));
    }

    public int getMetricAverage(String metricType, LocalDate from, LocalDate to) {
        return sensorReadingRepository.getAverageForMetric(metricType, from.atStartOfDay(), to.atTime(LocalTime.MAX));
    }

    public void deleteById(Long id) {
        LOG.debug("Deleting sensor reading {}", id);
        SensorReadingDao sensorReadingDao = sensorReadingRepository.getReferenceById(id);
        sensorReadingRepository.delete(sensorReadingDao);
    }

    private List<MetricValueDao> convert(List<MetricValue> metrics, SensorReadingDao sensorReading) {
        return metrics.stream().map(m -> {
            MetricDao metricDao = metricRepository.getReferenceById(m.getMetricId());
            return new MetricValueDao(metricDao, sensorReading, m.getMetricValue());
        }).collect(Collectors.toList());
    }

}
