package org.philbour.weatherservice.controller;

import org.philbour.weatherservice.model.SensorReading;
import org.philbour.weatherservice.model.resource.SensorReadingResource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
public class RegisterController {

    @PostMapping("/register")
    @ResponseStatus(code = HttpStatus.CREATED)
    SensorReading register(@Valid @RequestBody SensorReadingResource reading) {
        return new SensorReading("1", reading.getSensorId(), reading.getTimeOfReading(), reading.getMetrics());
    }

}
