package org.philbour.weatherservice.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
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

    @GetMapping("/metric/{type}")
    int metricAverage(@PathVariable("type") @NotBlank String type,
            @RequestParam("from") @DateTimeFormat(iso = ISO.DATE) @PastOrPresent(message = "Date must be before Today") LocalDate from,
            @RequestParam("to") @PastOrPresent(message = "Date must be on or before Today") @DateTimeFormat(iso = ISO.DATE) LocalDate to) {
        return 0;
    }

    @GetMapping("/sensor/{id}")
    int sensorAverage(@PathVariable("id") @NotBlank String id) {
        return 0;
    }
}
