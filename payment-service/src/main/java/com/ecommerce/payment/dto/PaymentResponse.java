package com.ecommerce.payment.dto;

import com.ecommerce.payment.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        Long orderId,
        Long customerId,
        BigDecimal amount,
        PaymentStatus status,
        LocalDateTime createdAt
) {
}
