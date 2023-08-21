package org.philbour.weatherservice.controller;

import org.philbour.weatherservice.model.Sensor;
import org.philbour.weatherservice.model.resource.SensorResource;
import org.philbour.weatherservice.service.SensorService;
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
@RequestMapping(value = "/sensor", produces = MediaType.APPLICATION_JSON_VALUE)
@Secured({"ROLE_ADMIN", "ROLE_USER"})
public class SensorController {

    private static final Logger LOG = LoggerFactory.getLogger(SensorController.class);

    @Autowired
    private SensorService sensorService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Sensor> register(@Valid @RequestBody SensorResource sensorResource) {
        LOG.debug("register request received for new sensor at location {}", sensorResource.getLocation());
        Sensor sensor = sensorService.register(sensorResource);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(sensor.getId()).toUri();
        return ResponseEntity.created(uri).body(sensor);
    }

    @GetMapping
    ResponseEntity<List<Sensor>> getAll() {
        LOG.debug("get request received for all sensors");
        return ResponseEntity.ok(sensorService.getAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<Sensor> getById(@PathVariable("id") @NotNull Long id) {
        LOG.debug("get request received for sensor {}", id);
        return ResponseEntity.of(sensorService.getById(id));
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    ResponseEntity<Sensor> deleteById(@PathVariable("id") @NotNull Long id) {
        LOG.debug("delete request received for sensor {}", id);
        sensorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
