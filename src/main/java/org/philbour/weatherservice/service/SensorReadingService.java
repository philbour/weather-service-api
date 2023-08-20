package org.philbour.weatherservice.service;

import org.philbour.weatherservice.model.MetricValue;
import org.philbour.weatherservice.model.SensorReading;
import org.philbour.weatherservice.model.dao.SensorDao;
import org.philbour.weatherservice.model.dao.SensorReadingDao;
import org.philbour.weatherservice.repository.SensorReadingRepository;
import org.philbour.weatherservice.repository.SensorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

@Service
public class SensorReadingService {

    private static final Logger LOG = LoggerFactory.getLogger(SensorReadingService.class);

    private final SensorReadingRepository sensorReadingRepository;
    private final SensorRepository sensorRepository;

    public SensorReadingService(SensorReadingRepository sensorReadingRepository, SensorRepository sensorRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
        this.sensorRepository = sensorRepository;
    }

    public SensorReading register(@NotNull SensorReading sensorReading) {
        LOG.debug("Saving new sensor reading");
        SensorDao sensor = sensorRepository.getReferenceById(sensorReading.getSensorId());
        SensorReadingDao sensorReadingDao = sensorReadingRepository.save(new SensorReadingDao(sensor, sensorReading
                .getTimeOfReading()));
        return new SensorReading(sensorReadingDao.getId(), sensorReadingDao.getSensor().getId(), sensorReadingDao
                .getTimeOfReading(), sensorReadingDao.getMetrics()
                        .stream()
                        .map(m -> new MetricValue(m.getMetric().getMetricType(), m.getMetricValue()))
                        .collect(Collectors.toList()));
    }

    /*
     * public List<Sensor> getAll() { return sensorRepository.findAll(); } public Optional<Sensor> getById(@NotNull Long
     * id) { return sensorRepository.findById(id); } public void deleteById(@NotNull Long id) { Sensor sensor =
     * sensorRepository.getReferenceById(id); sensorRepository.delete(sensor); }
     */

}
