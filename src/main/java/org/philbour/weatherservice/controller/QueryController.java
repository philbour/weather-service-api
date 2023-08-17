package org.philbour.weatherservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;

@RestController
@Validated
public class QueryController {

    private static final Logger LOG = LoggerFactory.getLogger(QueryController.class);

    @GetMapping("/metric/{type}")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    int metricAverage(@PathVariable("type") @NotBlank String type,
            @RequestParam("from") @DateTimeFormat(iso = ISO.DATE) @PastOrPresent(message = "Date must be before Today") LocalDate from,
            @RequestParam("to") @PastOrPresent(message = "Date must be on or before Today") @DateTimeFormat(iso = ISO.DATE) LocalDate to) {
        // TODO query DB but check cache first...

        LOG.debug("query request received for {} metric with date period of {} to {}", type, from, to);
        return 0;
    }

    @GetMapping("/sensor/{id}")
    int sensorAverage(@PathVariable("id") @NotBlank String id) {
        return 0;
    }
}
