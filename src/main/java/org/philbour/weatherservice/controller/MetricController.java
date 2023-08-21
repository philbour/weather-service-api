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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@Validated
@RequestMapping(value = "/metric", produces = MediaType.APPLICATION_JSON_VALUE)
@Secured({"ROLE_ADMIN", "ROLE_USER"})
public class MetricController {

    private static final Logger LOG = LoggerFactory.getLogger(MetricController.class);

    @Autowired
    private MetricService metricService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Save a new metric", description = "Saves a new metric from the data in the provided metricResource")
    @ApiResponse(responseCode = "201", description = "request was successful")
    @ApiResponse(responseCode = "400", description = "bad request")
    @ApiResponse(responseCode = "401", description = "forbidden")
    ResponseEntity<Metric> register(
            @Valid @RequestBody @Parameter(description = "The data to create the metric from") MetricResource metricResource) {
        LOG.debug("Register request received for new metric for type {}", metricResource.getMetricType());
        Metric metric = metricService.register(metricResource);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(metric.getId()).toUri();
        return ResponseEntity.created(uri).body(metric);
    }

    @GetMapping
    @Operation(summary = "Get all metrics", description = "Gets all the current metrics")
    @ApiResponse(responseCode = "200", description = "request was successful")
    @ApiResponse(responseCode = "400", description = "bad request")
    @ApiResponse(responseCode = "401", description = "forbidden")
    ResponseEntity<List<Metric>> getAll() {
        LOG.debug("Get request received for all metrics");
        return ResponseEntity.ok(metricService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a metric", description = "Gets a specific metric")
    @ApiResponse(responseCode = "200", description = "request was successful")
    @ApiResponse(responseCode = "400", description = "bad request")
    @ApiResponse(responseCode = "401", description = "forbidden")
    @ApiResponse(responseCode = "404", description = "not found")
    ResponseEntity<Metric> getById(
            @PathVariable("id") @Parameter(description = "The id of the metric") @NotNull Long id) {
        LOG.debug("Get request received for metric {}", id);
        return ResponseEntity.of(metricService.getById(id));
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    @Operation(summary = "Delete a metric", description = "Deletes a specific metric")
    @ApiResponse(responseCode = "204", description = "request was successful")
    @ApiResponse(responseCode = "400", description = "bad request")
    @ApiResponse(responseCode = "401", description = "forbidden")
    ResponseEntity<Metric> deleteById(
            @PathVariable("id") @Parameter(description = "The id of the metric") @NotNull Long id) {
        LOG.debug("Delete request received for metric {}", id);
        metricService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
