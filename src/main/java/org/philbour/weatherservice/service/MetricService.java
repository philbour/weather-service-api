package org.philbour.weatherservice.service;

import org.philbour.weatherservice.model.Metric;
import org.philbour.weatherservice.model.dao.MetricDao;
import org.philbour.weatherservice.repository.MetricRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

@Service
public class MetricService {

    private static final Logger LOG = LoggerFactory.getLogger(MetricService.class);

    private final MetricRepository metricRepository;

    public MetricService(MetricRepository metricRepository) {
        this.metricRepository = metricRepository;
    }

    public MetricDao register(@NotNull Metric metric) {
        LOG.debug("Saving new metric");
        return metricRepository.save(new MetricDao(metric));
    }

    public List<MetricDao> getAll() {
        return metricRepository.findAll();
    }

    public Optional<MetricDao> getById(@NotNull Long id) {
        return metricRepository.findById(id);
    }

    public void deleteById(@NotNull Long id) {
        MetricDao metric = metricRepository.getReferenceById(id);
        metricRepository.delete(metric);
    }

}
