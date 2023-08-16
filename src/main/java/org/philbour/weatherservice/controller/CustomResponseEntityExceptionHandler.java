package org.philbour.weatherservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> handle(ConstraintViolationException exception) {
        // you will get all javax failed validation, can be more than one
        // so you can return the set of error messages or just the first message
        String errorMessage = new ArrayList<>(exception.getConstraintViolations()).get(0).getMessage();
        // ApiError apiError = new ApiError(errorMessage, errorMessage, 1000);
        return new ResponseEntity<>(errorMessage, null, HttpStatus.BAD_REQUEST);
    }

}
