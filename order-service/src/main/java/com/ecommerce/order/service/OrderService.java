package com.ecommerce.order.service;

import com.ecommerce.order.client.CustomerClient;
import com.ecommerce.order.client.ProductClient;
import com.ecommerce.order.dto.CustomerResponse;
import com.ecommerce.order.dto.OrderRequest;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.dto.ProductRequest;
import com.ecommerce.order.dto.ProductResponse;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.entity.OrderStatus;
import com.ecommerce.order.event.OrderCreatedEvent;
import com.ecommerce.order.exception.CustomerNotFoundException;
import com.ecommerce.order.exception.InsufficientStockException;
import com.ecommerce.order.exception.OrderNotFoundException;
import com.ecommerce.order.exception.ProductNotFoundException;
import com.ecommerce.order.repository.OrderRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String orderTopic;

    public OrderService(
            OrderRepository orderRepository,
            CustomerClient customerClient,
            ProductClient productClient,
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${app.kafka.topics.order-created}") String orderTopic
    ) {
        this.orderRepository = orderRepository;
        this.customerClient = customerClient;
        this.productClient = productClient;
        this.kafkaTemplate = kafkaTemplate;
        this.orderTopic = orderTopic;
    }

    public OrderResponse create(OrderRequest request) {
        CustomerResponse customer = fetchCustomer(request.customerId());
        ProductResponse product = fetchProduct(request.productId());

        if (product.stock() < request.quantity()) {
            throw new InsufficientStockException(product.id(), request.quantity(), product.stock());
        }

        BigDecimal totalAmount = product.price().multiply(BigDecimal.valueOf(request.quantity()));

        Order order = new Order();
        order.setCustomerId(request.customerId());
        order.setProductId(request.productId());
        order.setQuantity(request.quantity());
        order.setUnitPrice(product.price());
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        productClient.updateProduct(
                product.id(),
                new ProductRequest(
                        product.name(),
                        product.description(),
                        product.price(),
                        product.stock() - request.quantity()
                )
        );

        kafkaTemplate.send(
                orderTopic,
                savedOrder.getId().toString(),
                new OrderCreatedEvent(
                        savedOrder.getId(),
                        customer.id(),
                        customer.email(),
                        product.id(),
                        product.name(),
                        request.quantity(),
                        totalAmount
                )
        );

        return toResponse(savedOrder);
    }

    public OrderResponse getById(Long id) {
        return toResponse(findOrder(id));
    }

    public List<OrderResponse> getAll() {
        return orderRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private CustomerResponse fetchCustomer(Long customerId) {
        try {
            return customerClient.getCustomer(customerId);
        } catch (FeignException.NotFound ex) {
            throw new CustomerNotFoundException(customerId);
        }
    }

    private ProductResponse fetchProduct(Long productId) {
        try {
            return productClient.getProduct(productId);
        } catch (FeignException.NotFound ex) {
            throw new ProductNotFoundException(productId);
        }
    }

    private Order findOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getProductId(),
                order.getQuantity(),
                order.getUnitPrice(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }

}
