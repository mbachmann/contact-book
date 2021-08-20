package com.example.contactbook.controller.advice;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class GlobalControllerAdvice  extends ResponseEntityExceptionHandler {


    @ExceptionHandler(Throwable.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<Object> handleInternalServiceError(Throwable t, WebRequest request) {
        return handleExceptionInternal(new RuntimeException(t), "Internal Server Error: " + t.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleInvalidBody(IllegalArgumentException ex, WebRequest request) {
        return handleExceptionInternal(ex, "Illegal Argument: " + ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex, "Resource not found: " + ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = ResourceAlreadyExistsException.class)
    protected ResponseEntity<Object> handleResourceAlreadExists(ResourceAlreadyExistsException ex, WebRequest request) {
        return handleExceptionInternal(ex, "Resource already exists: " + ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return this.handleExceptionInternal(ex, "Method argument not valid: " + ex.getMessage(), headers, status, request);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return this.handleExceptionInternal(ex, "Method argument not valid: " + ex.getMessage(), headers, status, request);
    }

}
