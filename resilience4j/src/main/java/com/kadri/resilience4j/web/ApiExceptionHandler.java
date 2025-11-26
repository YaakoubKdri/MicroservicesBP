package com.kadri.resilience4j.web;

import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.concurrent.TimeoutException;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(CallNotPermittedException.class)
    @ResponseStatus(SERVICE_UNAVAILABLE)
    public void handleCallNotPermitted(){

    }

    @ExceptionHandler(TimeoutException.class)
    @ResponseStatus(REQUEST_TIMEOUT)
    public void handleTimeout(){

    }

    @ExceptionHandler(BulkheadFullException.class)
    @ResponseStatus(BANDWIDTH_LIMIT_EXCEEDED)
    public void handleBulkheadFull(){

    }

    @ExceptionHandler(RequestNotPermitted.class)
    @ResponseStatus(TOO_MANY_REQUESTS)
    public void handleRequestNotPermitted(){

    }
}
