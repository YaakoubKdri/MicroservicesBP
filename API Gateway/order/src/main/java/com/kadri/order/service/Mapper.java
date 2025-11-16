package com.kadri.order.service;

import com.kadri.order.dto.OrderItemResponse;
import com.kadri.order.dto.OrderRequest;
import com.kadri.order.dto.OrderResponse;
import com.kadri.order.model.Order;
import com.kadri.order.model.OrderItem;
import com.kadri.order.model.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Mapper {

    public Order toEntity(OrderRequest request) {
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setStatus(OrderStatus.CREATED);

        List<OrderItem> items = request.getItems()
                .stream()
                .map(i -> OrderItem.builder()
                        .productId(i.getProductId())
                        .quantity(i.getQuantity())
                        .price(i.getPrice())
                        .order(order)
                        .build())
                .collect(Collectors.toList());
        order.setItems(items);
        return order;
    }

    public OrderResponse toResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems()
                .stream()
                .map(i -> OrderItemResponse.builder()
                        .productId(i.getProductId())
                        .quantity(i.getQuantity())
                        .price(i.getPrice())
                        .build())
                .collect(Collectors.toList());
        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .items(itemResponses)
                .build();
    }
}
