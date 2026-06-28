package com.ecommerce.order.client;

import com.ecommerce.order.dto.ProductRequest;
import com.ecommerce.order.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/v1/products/{id}")
    ProductResponse getProduct(@PathVariable("id") Long id);

    @PutMapping("/api/v1/products/{id}")
    ProductResponse updateProduct(@PathVariable("id") Long id, @RequestBody ProductRequest request);

}
