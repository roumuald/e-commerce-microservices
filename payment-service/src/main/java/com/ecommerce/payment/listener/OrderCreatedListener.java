package com.ecommerce.payment.listener;

import com.ecommerce.payment.event.OrderCreatedEvent;
import com.ecommerce.payment.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedListener {

    private static final Logger log = LoggerFactory.getLogger(OrderCreatedListener.class);

    private final PaymentService paymentService;

    public OrderCreatedListener(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @KafkaListener(topics = "${app.kafka.topics.order-created}", groupId = "payment-service")
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Received order-created event for order {}", event.orderId());
        paymentService.processOrderCreated(event);
    }

}
