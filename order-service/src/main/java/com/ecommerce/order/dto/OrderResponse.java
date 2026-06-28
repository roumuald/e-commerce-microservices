package com.ecommerce.order.dto;

import com.ecommerce.order.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
        Long id,
        Long customerId,
        Long productId,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal totalAmount,
        OrderStatus status,
        LocalDateTime createdAt
) {
}
