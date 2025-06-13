package com.neman.orderms.Client;

import com.neman.orderms.Dto.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "product-service", path = "/api/v1/products", url = "${PRODUCT_SERVICE_URL:http://nginx:4010}")
public interface ProductClient {

    @GetMapping("/get/{id}")
    ProductResponseDto getProductById(@PathVariable("id") Long id);

    @PostMapping("/decreaseStock/{productId}/{quantity}")
    void decreaseStock(@PathVariable("productId") Long productId, @PathVariable("quantity") int quantity);
}
