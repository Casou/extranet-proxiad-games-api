package com.proxiad.games.extranet.config;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Object entityNotFoundHandler(EntityNotFoundException e) {
        return new HttpEntity<>(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object exceptionHandler(Exception e) {
        return new HttpEntity<>(e.getMessage());
    }

}
