package com.kadri.resilience4j;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ActuatorAvailabilityTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void disableErrorHandler(){
        restTemplate.getRestTemplate().setErrorHandler(response -> false);
    }

    @Test
    void actuatorHealthIsUp(){
        ResponseEntity<String> response = restTemplate.getForEntity("/actuator/health", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("\"UP\"");
    }

    @Test
    void circuitBreakerEndpointIsAvailable(){
        ResponseEntity<String> response = restTemplate.getForEntity("/actuator/circuitbreakers", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void metricsEndpointIsAvailable(){
        ResponseEntity<String> response = restTemplate.getForEntity("/actuator/metrics", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
