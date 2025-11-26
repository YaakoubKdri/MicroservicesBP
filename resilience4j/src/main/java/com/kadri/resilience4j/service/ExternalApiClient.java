package com.kadri.resilience4j.service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ExternalApiClient {
    private final RestTemplate restTemplate;

    public ExternalApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getOk(){
        return restTemplate.getForObject("/external/ok", String.class);
    }

    public String callAlwaysFail(){
        return restTemplate.getForObject("/external/fail", String.class);
    }

    public String callSlow(){
        return restTemplate.getForObject("/external/slow", String.class);
    }
}
