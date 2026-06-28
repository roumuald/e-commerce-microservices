package com.ecommerce.order.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(Long productId, Integer requested, Integer available) {
        super("Insufficient stock for product " + productId + ": requested " + requested + ", available " + available);
    }

}
