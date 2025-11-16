package com.kadri.order.dto;

import com.kadri.order.model.OrderItem;
import com.kadri.order.model.OrderStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private Long userId;
    private BigDecimal totalAmount;
    private OrderStatus status = OrderStatus.CREATED;
    private List<OrderItemResponse> items = new ArrayList<>();
}
