package org.philbour.weatherservice.service;

import org.philbour.weatherservice.model.Sensor;
import org.philbour.weatherservice.repository.SensorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

@Service
public class SensorService {

    private static final Logger LOG = LoggerFactory.getLogger(SensorService.class);

    private final SensorRepository sensorRepository;

    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public Sensor register(@NotNull Sensor sensor) {
        LOG.debug("Saving new sensor");
        return sensorRepository.save(sensor);
    }

    public List<Sensor> getAll() {
        return sensorRepository.findAll();
    }

    public Optional<Sensor> getById(@NotNull Long id) {
        return sensorRepository.findById(id);
    }

    public void deleteById(@NotNull Long id) {
        Sensor sensor = sensorRepository.getReferenceById(id);
        sensorRepository.delete(sensor);
    }

}
