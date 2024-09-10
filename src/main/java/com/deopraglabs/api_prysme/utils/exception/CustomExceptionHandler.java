package com.deopraglabs.api_prysme.utils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.NoSuchElementException;

@ControllerAdvice
@RestController
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    // CUSTOM EXCEPTION HANDLER
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception e, WebRequest request) {
        return new ResponseEntity<>(new ExceptionResponse(
                new Date(),
                e.getMessage(),
                request.getDescription(false)), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    public final ResponseEntity<ExceptionResponse> handleNoSuchElementException(NoSuchElementException e, WebRequest request) {
        return new ResponseEntity<>(new ExceptionResponse(
                new Date(),
                e.getMessage(),
                request.getDescription(false)), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<ExceptionResponse> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, WebRequest request) {
        return new ResponseEntity<>(new ExceptionResponse(
                new Date(),
                String.format("Value '%s' not allowed for parameter '%s'. Expecting a '%s' type value.", e.getValue(), e.getName(), e.getRequiredType().getSimpleName()),
                request.getDescription(false)), HttpStatus.BAD_REQUEST
        );
    }

}
