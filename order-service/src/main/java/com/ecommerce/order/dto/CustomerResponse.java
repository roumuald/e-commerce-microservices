package com.ecommerce.order.dto;

import java.math.BigDecimal;

public record CustomerResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String address
) {
}
