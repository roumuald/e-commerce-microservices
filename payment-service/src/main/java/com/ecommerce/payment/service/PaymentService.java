package com.ecommerce.payment.service;

import com.ecommerce.payment.dto.PaymentResponse;
import com.ecommerce.payment.entity.Payment;
import com.ecommerce.payment.entity.PaymentStatus;
import com.ecommerce.payment.event.OrderCreatedEvent;
import com.ecommerce.payment.exception.PaymentNotFoundException;
import com.ecommerce.payment.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public void processOrderCreated(OrderCreatedEvent event) {
        if (paymentRepository.findByOrderId(event.orderId()).isPresent()) {
            log.warn("Payment already exists for order {}", event.orderId());
            return;
        }

        Payment payment = new Payment();
        payment.setOrderId(event.orderId());
        payment.setCustomerId(event.customerId());
        payment.setAmount(event.totalAmount());
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setCreatedAt(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);
        log.info("Payment {} processed for order {}", savedPayment.getId(), event.orderId());
    }

    public PaymentResponse getById(Long id) {
        return toResponse(findPayment(id));
    }

    public PaymentResponse getByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .map(this::toResponse)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found for order id: " + orderId));
    }

    public List<PaymentResponse> getAll() {
        return paymentRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private Payment findPayment(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException(id));
    }

    private PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrderId(),
                payment.getCustomerId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getCreatedAt()
        );
    }

}
