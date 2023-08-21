package org.philbour.weatherservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.philbour.weatherservice.model.Sensor;
import org.philbour.weatherservice.model.dao.SensorDao;
import org.philbour.weatherservice.model.resource.SensorResource;
import org.philbour.weatherservice.repository.SensorRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class SensorServiceTest {

    private SensorRepository mockSensorRepository;

    private SensorService sensorService;

    private static final String LOCATION = "location";
    private static final String LOCATION_2 = "location_2";
    private static final Long ID = 1L;

    @BeforeEach
    void setup() {
        mockSensorRepository = mock(SensorRepository.class);

        sensorService = new SensorService(mockSensorRepository);
    }

    @Test
    void register_SensorSaved_ReturnsSavedSensor() {
        when(mockSensorRepository.save(isA(SensorDao.class))).thenReturn(new SensorDao(LOCATION));

        Sensor sensor = sensorService.register(new SensorResource(LOCATION));

        assertEquals(LOCATION, sensor.getLocation());
    }

    @Test
    void getAll_SensorsFound_ReturnsSensors() {
        when(mockSensorRepository.findAll()).thenReturn(createSensors(LOCATION, LOCATION_2));

        List<Sensor> sensors = sensorService.getAll();

        assertThat(sensors).hasSize(2);
    }

    @Test
    void getAll_NoSensorsFound_ReturnsEmptyList() {
        when(mockSensorRepository.findAll()).thenReturn(null);

        List<Sensor> sensors = sensorService.getAll();

        assertThat(sensors).isEmpty();
    }

    @Test
    void getById_SensorFound_ReturnsSensor() {
        when(mockSensorRepository.findById(ID)).thenReturn(Optional.of(new SensorDao(LOCATION)));

        Optional<Sensor> sensor = sensorService.getById(ID);

        assertThat(sensor).isPresent();
    }

    @Test
    void getById_SensorNotFound_ReturnsEmpty() {
        when(mockSensorRepository.findById(ID)).thenReturn(Optional.empty());

        Optional<Sensor> sensor = sensorService.getById(ID);

        assertThat(sensor).isEmpty();
    }

    @Test
    void deleteById_SensorFound_DeletesSensor() {
        SensorDao sensorToDelete = new SensorDao(LOCATION);
        when(mockSensorRepository.getReferenceById(ID)).thenReturn(sensorToDelete);

        sensorService.deleteById(ID);

        verify(mockSensorRepository).delete(sensorToDelete);
    }

    @Test
    void deleteById_SensorNotFound_NoException() {
        when(mockSensorRepository.getReferenceById(ID)).thenReturn(null);

        sensorService.deleteById(ID);

        verify(mockSensorRepository).delete(null);
    }

    private List<SensorDao> createSensors(String... locations) {
        List<SensorDao> sensors = new ArrayList<>();
        Arrays.asList(locations).forEach(l -> sensors.add(new SensorDao(l)));
        return sensors;
    }

}
