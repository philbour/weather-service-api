package org.philbour.weatherservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.philbour.weatherservice.model.Metric;
import org.philbour.weatherservice.model.dao.MetricDao;
import org.philbour.weatherservice.model.resource.MetricResource;
import org.philbour.weatherservice.repository.MetricRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class MetricServiceTest {

    private MetricRepository mockMetricRepository;

    private MetricService metricService;

    private static final String TEMPERATURE_TYPE = "temperature";
    private static final String HUMIDITY_TYPE = "humidity";
    private static final Long ID = 1L;

    @BeforeEach
    void setup() {
        mockMetricRepository = mock(MetricRepository.class);

        metricService = new MetricService(mockMetricRepository);
    }

    @Test
    void register_MetricSaved_ReturnsSavedMetric() {
        when(mockMetricRepository.save(isA(MetricDao.class))).thenReturn(new MetricDao(TEMPERATURE_TYPE));

        Metric metric = metricService.register(new MetricResource(TEMPERATURE_TYPE));

        assertEquals(TEMPERATURE_TYPE, metric.getMetricType());
    }

    @Test
    void getAll_MetricsFound_ReturnsMetrics() {
        when(mockMetricRepository.findAll()).thenReturn(createMetrics(TEMPERATURE_TYPE, HUMIDITY_TYPE));

        List<Metric> metrics = metricService.getAll();

        assertThat(metrics).hasSize(2);
    }

    @Test
    void getAll_NoMetricsFound_ReturnsEmptyList() {
        when(mockMetricRepository.findAll()).thenReturn(null);

        List<Metric> metrics = metricService.getAll();

        assertThat(metrics).isEmpty();
    }

    @Test
    void getById_MetricFound_ReturnsMetric() {
        when(mockMetricRepository.findById(ID)).thenReturn(Optional.of(new MetricDao(TEMPERATURE_TYPE)));

        Optional<Metric> metric = metricService.getById(ID);

        assertThat(metric).isPresent();
    }

    @Test
    void getById_MetricNotFound_ReturnsEmpty() {
        when(mockMetricRepository.findById(ID)).thenReturn(Optional.empty());

        Optional<Metric> metric = metricService.getById(ID);

        assertThat(metric).isEmpty();
    }

    @Test
    void deleteById_MetricFound_DeletesMetric() {
        MetricDao metricToDelete = new MetricDao(HUMIDITY_TYPE);
        when(mockMetricRepository.getReferenceById(ID)).thenReturn(metricToDelete);

        metricService.deleteById(ID);

        verify(mockMetricRepository).delete(metricToDelete);
    }

    @Test
    void deleteById_MetricNotFound_NoException() {
        when(mockMetricRepository.getReferenceById(ID)).thenReturn(null);

        metricService.deleteById(ID);

        verify(mockMetricRepository).delete(null);
    }

    private List<MetricDao> createMetrics(String... types) {
        List<MetricDao> metrics = new ArrayList<>();
        Arrays.asList(types).forEach(t -> metrics.add(new MetricDao(t)));
        return metrics;
    }

}
