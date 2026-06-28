package com.ecommerce.notification.service;

import com.ecommerce.notification.dto.NotificationResponse;
import com.ecommerce.notification.entity.Notification;
import com.ecommerce.notification.event.OrderCreatedEvent;
import com.ecommerce.notification.exception.NotificationNotFoundException;
import com.ecommerce.notification.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;
    private final String mailFrom;

    public NotificationService(
            NotificationRepository notificationRepository,
            JavaMailSender mailSender,
            @Value("${app.mail.from}") String mailFrom
    ) {
        this.notificationRepository = notificationRepository;
        this.mailSender = mailSender;
        this.mailFrom = mailFrom;
    }

    public void processOrderCreated(OrderCreatedEvent event) {
        if (notificationRepository.findByOrderId(event.orderId()).isPresent()) {
            log.warn("Notification already exists for order {}", event.orderId());
            return;
        }

        String subject = "Order confirmation #" + event.orderId();
        String message = "Hello, your order #" + event.orderId()
                + " for " + event.productName()
                + " (x" + event.quantity() + ") has been placed. Total: " + event.totalAmount();

        Notification notification = new Notification();
        notification.setOrderId(event.orderId());
        notification.setCustomerId(event.customerId());
        notification.setCustomerEmail(event.customerEmail());
        notification.setSubject(subject);
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(mailFrom);
            mailMessage.setTo(event.customerEmail());
            mailMessage.setSubject(subject);
            mailMessage.setText(message);
            mailSender.send(mailMessage);
            notification.setSent(true);
            log.info("Email sent to {} for order {}", event.customerEmail(), event.orderId());
        } catch (Exception ex) {
            notification.setSent(false);
            log.error("Failed to send email for order {}", event.orderId(), ex);
        }

        notificationRepository.save(notification);
    }

    public NotificationResponse getById(String id) {
        return notificationRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found with id: " + id));
    }

    public NotificationResponse getByOrderId(Long orderId) {
        return notificationRepository.findByOrderId(orderId)
                .map(this::toResponse)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found for order id: " + orderId));
    }

    public List<NotificationResponse> getAll() {
        return notificationRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getOrderId(),
                notification.getCustomerId(),
                notification.getCustomerEmail(),
                notification.getSubject(),
                notification.getMessage(),
                notification.isSent(),
                notification.getCreatedAt()
        );
    }

}
