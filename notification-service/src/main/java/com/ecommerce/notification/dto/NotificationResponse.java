package com.ecommerce.notification.dto;

import java.time.LocalDateTime;

public record NotificationResponse(
        String id,
        Long orderId,
        Long customerId,
        String customerEmail,
        String subject,
        String message,
        boolean sent,
        LocalDateTime createdAt
) {
}
