package com.ecommerce.order.event;

import java.math.BigDecimal;

public record OrderCreatedEvent(
        Long orderId,
        Long customerId,
        String customerEmail,
        Long productId,
        String productName,
        Integer quantity,
        BigDecimal totalAmount
) {
}
