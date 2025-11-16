package com.kadri.order.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemRequest {
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
}
