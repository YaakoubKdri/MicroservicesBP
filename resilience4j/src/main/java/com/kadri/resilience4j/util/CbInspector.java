package com.kadri.resilience4j.util;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.springframework.stereotype.Component;

@Component
public class CbInspector {
    private final CircuitBreakerRegistry registry;

    public CbInspector(CircuitBreakerRegistry registry) {
        this.registry = registry;
    }

    public String getState(String name){
        CircuitBreaker cb = registry.circuitBreaker(name);
        return cb.getState().name();
    }

    public double getFailureRate(String name){
        CircuitBreaker cb = registry.circuitBreaker(name);
        return cb.getMetrics().getFailureRate();
    }
}
