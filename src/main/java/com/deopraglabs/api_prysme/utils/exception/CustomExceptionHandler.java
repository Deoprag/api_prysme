package com.deopraglabs.api_prysme.utils;

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

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception e, WebRequest request) {
        return new ResponseEntity<>(new ExceptionResponse(
                new Date(),
                e.getMessage(),
                request.getDescription(false)), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    public final ResponseEntity<ExceptionResponse> handleNoSuchElementException(Exception e, WebRequest request) {
        return new ResponseEntity<>(new ExceptionResponse(
                new Date(),
                e.getMessage(),
                request.getDescription(false)), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<ExceptionResponse> methodArgumentTypeMismatchException(Exception e, WebRequest request) {
        return new ResponseEntity<>(new ExceptionResponse(
                new Date(),
                e.getMessage(),
                request.getDescription(false)), HttpStatus.BAD_REQUEST
        );
    }
}
