package com.ecommerce.notification.controller;

import com.ecommerce.notification.dto.NotificationResponse;
import com.ecommerce.notification.service.NotificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/{id}")
    public NotificationResponse getById(@PathVariable String id) {
        return notificationService.getById(id);
    }

    @GetMapping("/order/{orderId}")
    public NotificationResponse getByOrderId(@PathVariable Long orderId) {
        return notificationService.getByOrderId(orderId);
    }

    @GetMapping
    public List<NotificationResponse> getAll() {
        return notificationService.getAll();
    }

}
