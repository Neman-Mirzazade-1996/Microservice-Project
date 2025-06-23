package com.neman.orderms.Client;

import com.neman.orderms.Dto.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "product-service", url = "${GATEWAY_HOST:http://localhost}", path = "/api/v1/products")
public interface ProductClient {

    @GetMapping("/get/{id}")
    ProductResponseDto getProductById(@PathVariable("id") Long id);

    @PostMapping("/decreaseStock/{productId}/{quantity}")
    void decreaseStock(@PathVariable("productId") Long productId, @PathVariable("quantity") int quantity);
}
