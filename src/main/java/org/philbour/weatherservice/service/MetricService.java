package org.philbour.weatherservice.service;

import org.apache.commons.collections4.CollectionUtils;
import org.philbour.weatherservice.model.Metric;
import org.philbour.weatherservice.model.dao.MetricDao;
import org.philbour.weatherservice.model.resource.MetricResource;
import org.philbour.weatherservice.repository.MetricRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MetricService {

    private static final Logger LOG = LoggerFactory.getLogger(MetricService.class);

    private final MetricRepository metricRepository;

    public MetricService(MetricRepository metricRepository) {
        this.metricRepository = metricRepository;
    }

    public Metric register(MetricResource metricResource) {
        LOG.debug("Saving new metric");
        return new Metric(metricRepository.save(new MetricDao(metricResource.getMetricType())));
    }

    public List<Metric> getAll() {
        LOG.debug("Getting all metrics");
        List<MetricDao> metrics = metricRepository.findAll();

        if (CollectionUtils.isNotEmpty(metrics)) {
            return metrics.stream().map(Metric::new).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    public Optional<Metric> getById(Long id) {
        LOG.debug("Getting metric {}", id);
        Optional<MetricDao> optional = metricRepository.findById(id);
        return optional.isPresent() ? Optional.of(new Metric(optional.get())) : Optional.empty();
    }

    public void deleteById(Long id) {
        LOG.debug("Deleting metric {}", id);
        MetricDao metric = metricRepository.getReferenceById(id);
        metricRepository.delete(metric);
    }

}
