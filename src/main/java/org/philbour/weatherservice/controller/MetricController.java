package org.philbour.weatherservice.controller;

import org.philbour.weatherservice.model.Metric;
import org.philbour.weatherservice.model.resource.MetricResource;
import org.philbour.weatherservice.service.MetricService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@Validated
@RequestMapping(value = "/metric", produces = MediaType.APPLICATION_JSON_VALUE)
@Secured({"ROLE_ADMIN", "ROLE_USER"})
public class MetricController {

    private static final Logger LOG = LoggerFactory.getLogger(MetricController.class);

    @Autowired
    private MetricService metricService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Metric> register(@Valid @RequestBody MetricResource metricResource) {
        LOG.debug("register request received for new metric for type {}", metricResource.getMetricType());
        Metric metric = metricService.register(metricResource);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(metric.getId()).toUri();
        return ResponseEntity.created(uri).body(metric);
    }

    @GetMapping
    ResponseEntity<List<Metric>> getAll() {
        LOG.debug("get request received for all metrics");
        return ResponseEntity.ok(metricService.getAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<Metric> getById(@PathVariable("id") @NotNull Long id) {
        LOG.debug("get request received for metric {}", id);
        return ResponseEntity.of(metricService.getById(id));
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    ResponseEntity<Metric> deleteById(@PathVariable("id") @NotNull Long id) {
        LOG.debug("delete request received for metric {}", id);
        metricService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
