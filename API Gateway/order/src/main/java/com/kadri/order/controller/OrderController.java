package com.kadri.order.controller;

import com.kadri.order.dto.OrderRequest;
import com.kadri.order.dto.OrderResponse;
import com.kadri.order.service.Mapper;
import com.kadri.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;
    private final Mapper mapper;

    @PostMapping
    public OrderResponse create(@RequestBody OrderRequest request){
        return mapper.toResponse(service.createOrder(request));
    }

    @GetMapping("/{id}")
    public OrderResponse get(@PathVariable Long id){
        return mapper.toResponse(service.getById(id));
    }

    @GetMapping("/by-user/{userId}")
    public List<OrderResponse> findByUser(@PathVariable Long userId){
        return service.getByUserId(userId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}
