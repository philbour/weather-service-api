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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@Validated
@RequestMapping(value = "/reading", produces = MediaType.APPLICATION_JSON_VALUE)
@Secured({"ROLE_ADMIN", "ROLE_USER"})
public class SensorReadingController {

    private static final Logger LOG = LoggerFactory.getLogger(SensorReadingController.class);

    @Autowired
    private SensorReadingService sensorReadingService;

    @PostMapping
    @Operation(summary = "Save a new sensor reading", description = "Saves a new sensor reading from the data in the provided sensorResource")
    @ApiResponse(responseCode = "201", description = "request was successful")
    @ApiResponse(responseCode = "400", description = "bad request")
    @ApiResponse(responseCode = "401", description = "forbidden")
    ResponseEntity<SensorReading> register(
            @Valid @RequestBody @Parameter(description = "The data to create the sensor reading from") SensorReadingResource reading) {
        LOG.debug("Register request received for sensor {} reading", reading.getSensorId());
        SensorReading sensorReading = sensorReadingService.register(reading);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(sensorReading.getId())
                .toUri();
        return ResponseEntity.created(uri).body(sensorReading);
    }

    @GetMapping
    @Operation(summary = "Get all sensor readings", description = "Gets all the current sensor readings")
    @ApiResponse(responseCode = "200", description = "request was successful")
    @ApiResponse(responseCode = "400", description = "bad request")
    @ApiResponse(responseCode = "401", description = "forbidden")
    ResponseEntity<List<SensorReading>> getAll() {
        LOG.debug("Get request received for all sensor readings");
        return ResponseEntity.ok(sensorReadingService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a sensor reading", description = "Gets a specific sensor reading")
    @ApiResponse(responseCode = "200", description = "request was successful")
    @ApiResponse(responseCode = "400", description = "bad request")
    @ApiResponse(responseCode = "401", description = "forbidden")
    @ApiResponse(responseCode = "404", description = "not found")
    ResponseEntity<SensorReading> getById(
            @PathVariable("id") @Parameter(description = "The id of the sensor reading") @NotNull Long id) {
        LOG.debug("Get request received for sensor reading {}", id);
        return ResponseEntity.of(sensorReadingService.getById(id));
    }

    @GetMapping("/sensor/{id}/metric/{type}")
    @Operation(summary = "Get average metric sensor value", description = "Gets the average value of a metric for a specific sensor")
    @ApiResponse(responseCode = "200", description = "request was successful")
    @ApiResponse(responseCode = "400", description = "bad request")
    @ApiResponse(responseCode = "401", description = "forbidden")
    @ApiResponse(responseCode = "404", description = "not found")
    ResponseEntity<Integer> getSensorValueByMetric(
            @PathVariable("id") @Parameter(description = "The id of the sensor") @NotNull Long id,
            @PathVariable("type") @Parameter(description = "The metric type") @NotNull String metricType,
            @RequestParam("queryType") @Parameter(description = "The query type") @NotNull String queryType,
            @RequestParam("from") @Parameter(description = "Start of date range") @NotNull @DateTimeFormat(iso = ISO.DATE) LocalDate from,
            @RequestParam("to") @Parameter(description = "End of date range") @NotNull @DateTimeFormat(iso = ISO.DATE) LocalDate to) {
        LOG.debug("Get request received for {} {} metric value for sensor {}", queryType, metricType, id);
        return ResponseEntity.ok(sensorReadingService.getMetricAverageBySensorId(id, metricType, from, to));
    }

    @GetMapping("/sensor/metric/{type}")
    @Operation(summary = "Get average metric value", description = "Gets the average value of a metric for across all sensors")
    @ApiResponse(responseCode = "200", description = "request was successful")
    @ApiResponse(responseCode = "400", description = "bad request")
    @ApiResponse(responseCode = "401", description = "forbidden")
    @ApiResponse(responseCode = "404", description = "not found")
    ResponseEntity<Integer> getValueByMetric(
            @PathVariable("type") @Parameter(description = "The metric type") @NotNull String metricType,
            @RequestParam("queryType") @Parameter(description = "The query type") @NotNull String queryType,
            @RequestParam("from") @Parameter(description = "Start of date range") @NotNull @DateTimeFormat(iso = ISO.DATE) LocalDate from,
            @RequestParam("to") @Parameter(description = "End of date range") @NotNull @DateTimeFormat(iso = ISO.DATE) LocalDate to) {
        LOG.debug("Get request received for {} {} for all sensors", queryType, metricType);
        return ResponseEntity.ok(sensorReadingService.getMetricAverage(metricType, from, to));
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    @Operation(summary = "Delete a sensor reading", description = "Deletes a specific sensor reading")
    @ApiResponse(responseCode = "204", description = "request was successful")
    @ApiResponse(responseCode = "400", description = "bad request")
    @ApiResponse(responseCode = "401", description = "forbidden")
    ResponseEntity<SensorReading> deleteById(
            @PathVariable("id") @Parameter(description = "The id of the sensor reading") @NotNull Long id) {
        LOG.debug("Delete request received for sensor reading {}", id);
        sensorReadingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
