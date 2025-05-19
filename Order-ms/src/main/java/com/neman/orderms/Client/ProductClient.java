package com.neman.orderms.Client;

import com.neman.orderms.Dto.ProductResponseDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "product-service", url = "http://product-service:8097/api/v1/products")
public interface ProductClient {
    @GetMapping
    ProductResponseDto getProductById(@PathVariable("id") Long id);
    @PutMapping("/decrease-stock/{id}")
    void decreaseStock(@PathVariable("id") Long productId, @RequestParam("quantity") int quantity);
}
