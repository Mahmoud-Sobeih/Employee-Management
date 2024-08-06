package com.project.config.exception;

import com.project.config.exception.exceptions.AuthenticationException;
import com.project.config.exception.exceptions.InternalException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.project.config.exception.exceptions.ResourceNotFoundException;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handelNotFoundException(ResourceNotFoundException ex){
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(), ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> authenticationExceptionHandler(AuthenticationException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.name(), ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InternalException.class)
    public ResponseEntity<ExceptionResponse> internalExceptionHandler(InternalException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(), ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
