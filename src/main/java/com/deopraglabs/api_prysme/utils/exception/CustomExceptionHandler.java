package com.deopraglabs.api_prysme.utils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.DateTimeException;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Objects;

@RestControllerAdvice
public class CustomExceptionHandler {

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
    public final ResponseEntity<ExceptionResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, WebRequest request) {
        return new ResponseEntity<>(new ExceptionResponse(
                new Date(),
                String.format("Value '%s' not allowed for parameter '%s'. Expecting a '%s' type value.", e.getValue(), e.getName(), Objects.requireNonNull(e.getRequiredType()).getSimpleName()),
                request.getDescription(false)), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(CustomRuntimeException.BRValidationException.class)
    public final ResponseEntity<ExceptionResponseList> handleBRValidationException(CustomRuntimeException.BRValidationException e, WebRequest request) {
        return new ResponseEntity<>(new ExceptionResponseList(
                new Date(),
                e.getErrors(),
                request.getDescription(false)), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(DateTimeException.class)
    public final ResponseEntity<ExceptionResponse> handleDateTimeException(DateTimeException e, WebRequest request) {
        return new ResponseEntity<>(new ExceptionResponse(
                new Date(),
                e.getMessage(),
                request.getDescription(false)), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException e, WebRequest request) {
        return new ResponseEntity<>(new ExceptionResponse(
                new Date(),
                e.getMostSpecificCause().getMessage(),
                request.getDescription(false)), HttpStatus.BAD_REQUEST
        );
    }
}
