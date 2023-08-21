package org.philbour.weatherservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class SensorReadingServiceTest {

    private SensorReadingRepository mockSensorReadingRepository;
    private SensorRepository mockSensorRepository;
    private MetricRepository mockMetricRepository;

    private SensorDao sensorDao;
    private MetricDao metricDao;
    private MetricValueDao metricValueDao;
    private SensorReadingDao sensorReadingDao;

    private SensorReadingService sensorReadingService;

    private static final String LOCATION = "location";
    private static final String TEMPERATURE_TYPE = "temperature";
    private static final Long ID = 1L;
    private static final Long SENSOR_ID = 2L;
    private static final Long METRIC_ID = 3L;

    @BeforeEach
    void setup() {
        mockSensorReadingRepository = mock(SensorReadingRepository.class);
        mockSensorRepository = mock(SensorRepository.class);
        mockMetricRepository = mock(MetricRepository.class);

        sensorDao = new SensorDao(SENSOR_ID, LOCATION);
        when(mockSensorRepository.getReferenceById(SENSOR_ID)).thenReturn(new SensorDao(SENSOR_ID, LOCATION));

        metricDao = new MetricDao(METRIC_ID, TEMPERATURE_TYPE);
        when(mockMetricRepository.getReferenceById(METRIC_ID)).thenReturn(metricDao);

        sensorReadingDao = new SensorReadingDao(ID, sensorDao, LocalDateTime.now());
        metricValueDao = new MetricValueDao(metricDao, sensorReadingDao, 100);

        sensorReadingService = new SensorReadingService(mockSensorReadingRepository, mockSensorRepository,
                mockMetricRepository);
    }

    @Test
    void register_SensorReadingSaved_ReturnsSavedSensorReading() {
        when(mockSensorReadingRepository.save(isA(SensorReadingDao.class))).thenReturn(sensorReadingDao);

        SensorReading sensorReading = sensorReadingService.register(new SensorReadingResource(SENSOR_ID, LocalDateTime
                .now(), Arrays.asList(new MetricValue(metricValueDao))));

        assertEquals(SENSOR_ID, sensorReading.getSensorId());
    }

    @Test
    void getAll_ReadingsFound_ReturnsReadings() {
        when(mockSensorReadingRepository.findAll()).thenReturn(Arrays.asList(sensorReadingDao));

        List<SensorReading> readings = sensorReadingService.getAll();

        assertThat(readings).hasSize(1);
    }

    @Test
    void getAll_NoReadingsFound_ReturnsEmptyList() {
        when(mockSensorReadingRepository.findAll()).thenReturn(null);

        List<SensorReading> readings = sensorReadingService.getAll();

        assertThat(readings).isEmpty();
    }

    @Test
    void getById_ReadingFound_ReturnsReading() {
        when(mockSensorReadingRepository.findById(ID)).thenReturn(Optional.of(sensorReadingDao));

        Optional<SensorReading> reading = sensorReadingService.getById(ID);

        assertThat(reading).isPresent();
    }

    @Test
    void getById_ReadingNotFound_ReturnsEmpty() {
        when(mockSensorReadingRepository.findById(ID)).thenReturn(Optional.empty());

        Optional<SensorReading> reading = sensorReadingService.getById(ID);

        assertThat(reading).isEmpty();
    }

    @Test
    void deleteById_ReadingFound_DeletesReading() {
        when(mockSensorReadingRepository.getReferenceById(ID)).thenReturn(sensorReadingDao);

        sensorReadingService.deleteById(ID);

        verify(mockSensorReadingRepository).delete(sensorReadingDao);
    }

    @Test
    void deleteById_SensorNotFound_NoException() {
        when(mockSensorReadingRepository.getReferenceById(ID)).thenReturn(null);

        sensorReadingService.deleteById(ID);

        verify(mockSensorReadingRepository).delete(null);
    }

    @Test
    void getMetricAverageBySensorId_ValidValues_ReturnsAverageValue() {
        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now();
        when(mockSensorReadingRepository.getAverageForMetric(SENSOR_ID, TEMPERATURE_TYPE, from.atStartOfDay(), to
                .atTime(LocalTime.MAX))).thenReturn(1);

        int val = sensorReadingService.getMetricAverageBySensorId(SENSOR_ID, TEMPERATURE_TYPE, from, to);

        assertEquals(1, val);
    }

    @Test
    void getMetricAverage_ValidValues_ReturnsAverageValue() {
        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now();
        when(mockSensorReadingRepository.getAverageForMetric(TEMPERATURE_TYPE, from.atStartOfDay(), to.atTime(
                LocalTime.MAX))).thenReturn(10);

        int val = sensorReadingService.getMetricAverage(TEMPERATURE_TYPE, from, to);

        assertEquals(10, val);
    }

}
