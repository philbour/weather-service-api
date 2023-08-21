package org.philbour.weatherservice.service;

import org.apache.commons.collections4.CollectionUtils;
import org.philbour.weatherservice.model.Sensor;
import org.philbour.weatherservice.model.dao.SensorDao;
import org.philbour.weatherservice.model.resource.SensorResource;
import org.philbour.weatherservice.repository.SensorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SensorService {

    private static final Logger LOG = LoggerFactory.getLogger(SensorService.class);

    private final SensorRepository sensorRepository;

    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public Sensor register(SensorResource sensorResource) {
        LOG.debug("Saving new sensor");
        return new Sensor(sensorRepository.save(new SensorDao(sensorResource.getLocation())));
    }

    public List<Sensor> getAll() {
        LOG.debug("Getting all sensors");
        List<SensorDao> sensors = sensorRepository.findAll();

        if (CollectionUtils.isNotEmpty(sensors)) {
            return sensors.stream().map(Sensor::new).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    public Optional<Sensor> getById(Long id) {
        LOG.debug("Getting sensor {}", id);
        Optional<SensorDao> optional = sensorRepository.findById(id);
        return optional.isPresent() ? Optional.of(new Sensor(optional.get())) : Optional.empty();
    }

    public void deleteById(Long id) {
        LOG.debug("Deleting sensor {}", id);
        SensorDao sensor = sensorRepository.getReferenceById(id);
        sensorRepository.delete(sensor);
    }

}
