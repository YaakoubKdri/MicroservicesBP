package com.kadri.api_gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
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

        return orderMono.flatMap(order -> {
            Long userId = ((Number)order.get("userId")).longValue();
            List<Integer> productIds = extractProductsIds(order);

            Mono<Map> userMono = webClient.get()
                    .uri("http://user-service/api/v1/users/{userId}", userId)
                    .retrieve()
                    .bodyToMono(Map.class);

            Mono<List> productMono = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("http")
                            .host("product-service")
                            .path("/products/by-ids")
                            .queryParam("ids", productIds)
                            .build()
                    )
                    .retrieve()
                    .bodyToMono(List.class);

            return Mono.zip(userMono, productMono)
                    .map(tuple -> {
                        Map<String, Object> result = new HashMap<>();
                        result.put("order", order);
                        result.put("user", tuple.getT1());
                        result.put("products", tuple.getT2());
                        return result;
                    });
        });

    }

    @SuppressWarnings("unchecked")
    private List<Long> extractProductsIds(Map<String, Object> order) {
        List<Map<String, Object>> items =
                (List<Map<String, Object>>) order.get("items");

        if (items == null) return List.of();

        return items.stream()
                .map(item -> ((Number) item.get("productId")).longValue())
                .toList();
    }
}
