package org.philbour.weatherservice.controller;

import org.philbour.weatherservice.model.SensorReading;
import org.philbour.weatherservice.model.resource.SensorReadingResource;
import org.philbour.weatherservice.service.SensorReadingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import javax.validation.Valid;

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
        SensorReading sensorReading = sensorReadingService.register(new SensorReading(reading));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(sensorReading.getId())
                .toUri();
        return ResponseEntity.created(uri).body(sensorReading);
    }

}
