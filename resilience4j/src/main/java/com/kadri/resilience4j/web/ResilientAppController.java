package com.kadri.resilience4j.web;

import com.kadri.resilience4j.service.ExternalApiClient;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping
public class ResilientAppController {

    private final ExternalApiClient externalApiClient;

    public ResilientAppController(ExternalApiClient externalApiClient) {
        this.externalApiClient = externalApiClient;
    }

    @GetMapping("/circuit-breaker")
    @CircuitBreaker(name = "circuitBreakerService")
    public String circuitBreakerExample(){
        return externalApiClient.callAlwaysFail();
    }

    @GetMapping("/retry")
    @Retry(name = "retryApi", fallbackMethod = "retryFallback")
    public String retryExample(){
        return externalApiClient.callAlwaysFail();
    }

    public String retryFallback(Exception exception){
        return "All retry attempts failed, returning fallback response";
    }

    @GetMapping("/time-limiter")
    @TimeLimiter(name = "timeLimiterApi")
    public CompletableFuture<String> timeLimiterExample(){
        return CompletableFuture.supplyAsync(externalApiClient::callSlow);
    }

    @GetMapping("/bulkhead")
    @Bulkhead(name = "bulkheadApi")
    public String bulkheadExample(){
        return externalApiClient.callSlow();
    }

    @GetMapping("/rate-limiter")
    @RateLimiter(name = "rateLimiterApi")
    public String rateLimiterExample(){
        return externalApiClient.getOk();
    }
}
