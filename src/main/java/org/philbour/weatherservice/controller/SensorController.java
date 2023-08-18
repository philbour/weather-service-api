package org.philbour.weatherservice.controller;

import org.philbour.weatherservice.model.Sensor;
import org.philbour.weatherservice.model.resource.SensorResource;
import org.philbour.weatherservice.service.SensorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

@RestController
@Validated
@RequestMapping("/sensor")
public class SensorController {

    private static final Logger LOG = LoggerFactory.getLogger(SensorController.class);

    @Autowired
    private SensorService sensorService;

    @PostMapping(value = "/register", consumes = "application/json")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @ResponseStatus(code = HttpStatus.CREATED)
    Sensor register(@Valid @RequestBody SensorResource sensorResource) {
        LOG.debug("register request received for new sensor at location {}", sensorResource.getLocation());
        return sensorService.register(new Sensor(sensorResource.getLocation()));
    }

    @GetMapping(value = "/get")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    ResponseEntity<List<Sensor>> get() {
        LOG.debug("get request received for all sensors");
        return ResponseEntity.ok(sensorService.getAll());
    }

}
