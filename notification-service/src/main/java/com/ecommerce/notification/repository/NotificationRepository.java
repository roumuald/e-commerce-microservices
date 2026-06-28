package com.ecommerce.notification.repository;

import com.ecommerce.notification.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends MongoRepository<Notification, String> {

    Optional<Notification> findByOrderId(Long orderId);

    List<Notification> findAllByOrderId(Long orderId);

}
