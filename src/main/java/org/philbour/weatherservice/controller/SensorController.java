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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@Validated
@RequestMapping(value = "/sensor", produces = MediaType.APPLICATION_JSON_VALUE)
@Secured({"ROLE_ADMIN", "ROLE_USER"})
public class SensorController {

    private static final Logger LOG = LoggerFactory.getLogger(SensorController.class);

    @Autowired
    private SensorService sensorService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Save a new sensor", description = "Saves a new sensor from the data in the provided sensorResource")
    @ApiResponse(responseCode = "201", description = "request was successful")
    @ApiResponse(responseCode = "400", description = "bad request")
    @ApiResponse(responseCode = "401", description = "forbidden")
    ResponseEntity<Sensor> register(
            @Valid @RequestBody @Parameter(description = "The data to create the sensor from") SensorResource sensorResource) {
        LOG.debug("Register request received for new sensor at location {}", sensorResource.getLocation());
        Sensor sensor = sensorService.register(sensorResource);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(sensor.getId()).toUri();
        return ResponseEntity.created(uri).body(sensor);
    }

    @GetMapping
    @Operation(summary = "Get all sensors", description = "Gets all the current sensors")
    @ApiResponse(responseCode = "200", description = "request was successful")
    @ApiResponse(responseCode = "400", description = "bad request")
    @ApiResponse(responseCode = "401", description = "forbidden")
    ResponseEntity<List<Sensor>> getAll() {
        LOG.debug("Get request received for all sensors");
        return ResponseEntity.ok(sensorService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a sensor", description = "Gets a specific sensor")
    @ApiResponse(responseCode = "200", description = "request was successful")
    @ApiResponse(responseCode = "400", description = "bad request")
    @ApiResponse(responseCode = "401", description = "forbidden")
    @ApiResponse(responseCode = "404", description = "not found")
    ResponseEntity<Sensor> getById(
            @PathVariable("id") @Parameter(description = "The id of the sensor") @NotNull Long id) {
        LOG.debug("Get request received for sensor {}", id);
        return ResponseEntity.of(sensorService.getById(id));
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    @Operation(summary = "Delete a sensor", description = "Deletes a specific sensor")
    @ApiResponse(responseCode = "204", description = "request was successful")
    @ApiResponse(responseCode = "400", description = "bad request")
    @ApiResponse(responseCode = "401", description = "forbidden")
    ResponseEntity<Sensor> deleteById(
            @PathVariable("id") @Parameter(description = "The id of the metric") @NotNull Long id) {
        LOG.debug("Delete request received for sensor {}", id);
        sensorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
