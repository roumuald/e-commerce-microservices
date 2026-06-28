package com.ecommerce.notification.listener;

import com.ecommerce.notification.event.OrderCreatedEvent;
import com.ecommerce.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedListener {

    private static final Logger log = LoggerFactory.getLogger(OrderCreatedListener.class);

    private final NotificationService notificationService;

    public OrderCreatedListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "${app.kafka.topics.order-created}", groupId = "notification-service")
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Received order-created event for order {}", event.orderId());
        notificationService.processOrderCreated(event);
    }

}
