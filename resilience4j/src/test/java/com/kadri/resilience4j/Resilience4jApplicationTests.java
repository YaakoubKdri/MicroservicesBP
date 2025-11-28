package com.kadri.resilience4j;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class Resilience4jApplicationTests {

	@Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void turnOffDefaultErrorHandling() {
        restTemplate.getRestTemplate().setErrorHandler(new ResponseErrorHandler() {

            @Override
            public boolean hasError(ClientHttpResponse response) {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) {
            }
        });
    }


    @Test
    void whenRetryExhausted_thenFallbackIsReturned(){
        ResponseEntity<String> response = restTemplate.getForEntity("/api/retry", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("All retry attempts failed, returning fallback response");
    }

    @Test
    void whenCallIsTooSlow_thenTimeLimiterReturnsTimeoutStatus(){
        ResponseEntity<String> response = restTemplate.getForEntity("/api/time-limiter", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.REQUEST_TIMEOUT);
    }

    @Test
    void whenRateLimitExceeded_thenTooManyRequestsReturned(){
        int allowed = 5;

        for(int i = 0; i < allowed; ++i){
            ResponseEntity<String> response = restTemplate.getForEntity("/api/rate-limiter", String.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo("external success");
        }
        ResponseEntity<String> limitedResponse = restTemplate.getForEntity("/api//rate-limiter", String.class);
        assertThat(limitedResponse.getStatusCode()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
    }

    @Test
    void whenFailuresAccumulate_thenCircuitBreakerOpens(){
        ResponseEntity<String> lastResponse = null;

        for(int i = 0; i < 10; ++i){
            lastResponse = restTemplate.getForEntity("/api/circuit-breaker", String.class);
        }

        assertThat(lastResponse).isNotNull();
        assertThat(lastResponse.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);

    }

    @Test
    void whenTooManyConcurrentCalls_thenBulkheadRejectsSome() throws InterruptedException {
        int threads = 30;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        List<Future<ResponseEntity<String>>> futures = new ArrayList<>();

        for(int i = 0; i < threads; ++i){
            futures.add(executor.submit(() -> restTemplate.getForEntity("/api/bulkhead", String.class)));
        }

        executor.shutdown();
        executor.awaitTermination(30, SECONDS);

        long rejectedCount = futures.stream()
                .map( f -> {
                    try {
                        return f.get();
                    }catch (Exception e){
                        throw new RuntimeException(e);
                    }
                })
                .filter(r -> r.getStatusCode().equals(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED))
                .count();

        assertThat(rejectedCount).isGreaterThan(0);
    }
}
