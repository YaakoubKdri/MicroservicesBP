package com.kadri.api_gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/aggregated/orders")
public class OrderAggregationController {

    private final WebClient webClient;

    public OrderAggregationController(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    @GetMapping("/{id}")
    public Mono<Map<String, Object>> getOrderDetails(@PathVariable String id){
        Mono<Map> orderMono = webClient.get()
                .uri("http://order-service/api/v1/orders/{id}", id)
                .retrieve()
                .bodyToMono(Map.class);

        Mono<Map> productMono = webClient.get()
                .uri("http://product-service/api/v1/products/by-order/{id}", id)
                .retrieve()
                .bodyToMono(Map.class);

        Mono<Map> userMono = webClient.get()
                .uri("http://user-service/api/v1/users/by-order/{id}", id)
                .retrieve()
                .bodyToMono(Map.class);

        return Mono.zip(orderMono, productMono, userMono)
                .map(tuple -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("order", tuple.getT1());
                    result.put("product", tuple.getT2());
                    result.put("user", tuple.getT3());
                    return result;
                });
    }
}
