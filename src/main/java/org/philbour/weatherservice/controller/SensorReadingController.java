package org.philbour.weatherservice.controller;

import org.philbour.weatherservice.model.SensorReading;
import org.philbour.weatherservice.model.resource.SensorReadingResource;
import org.philbour.weatherservice.service.SensorReadingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@Validated
@RequestMapping(value = "/reading", produces = MediaType.APPLICATION_JSON_VALUE)
@Secured({"ROLE_ADMIN", "ROLE_USER"})
public class SensorReadingController {

    private static final Logger LOG = LoggerFactory.getLogger(SensorReadingController.class);

    @Autowired
    private SensorReadingService sensorReadingService;

    @PostMapping
    ResponseEntity<SensorReading> register(@Valid @RequestBody SensorReadingResource reading) {
        LOG.debug("register request received for sensor {} reading", reading.getSensorId());
        SensorReading sensorReading = sensorReadingService.register(reading);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(sensorReading.getId())
                .toUri();
        return ResponseEntity.created(uri).body(sensorReading);
    }

    @GetMapping
    ResponseEntity<List<SensorReading>> getAll() {
        LOG.debug("get request received for all sensor readings");
        return ResponseEntity.ok(sensorReadingService.getAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<SensorReading> getById(@PathVariable("id") @NotNull Long id) {
        LOG.debug("get request received for sensor reading {}", id);
        return ResponseEntity.of(sensorReadingService.getById(id));
    }

    @GetMapping("/sensor/{id}/metric/{type}")
    ResponseEntity<Integer> getSensorValueByMetric(@PathVariable("id") Long id, @PathVariable("type") String metricType,
            @RequestParam("queryType") String queryType,
            @RequestParam("from") @DateTimeFormat(iso = ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = ISO.DATE) LocalDate to) {
        LOG.debug("get request received for {} {} metric value for sensor {}", queryType, metricType, id);
        return ResponseEntity.ok(sensorReadingService.getMetricAverageBySensorId(id, metricType, from, to));
    }

    @GetMapping("/sensor/metric/{type}")
    ResponseEntity<Integer> getValueByMetric(@PathVariable("type") String metricType,
            @RequestParam("queryType") String queryType,
            @RequestParam("from") @DateTimeFormat(iso = ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = ISO.DATE) LocalDate to) {
        LOG.debug("get request received for {} {} for all sensors", queryType, metricType);
        return ResponseEntity.ok(sensorReadingService.getMetricAverage(metricType, from, to));
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    ResponseEntity<SensorReading> deleteById(@PathVariable("id") @NotNull Long id) {
        LOG.debug("delete request received for sensor reading {}", id);
        sensorReadingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
