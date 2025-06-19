package com.neman.orderms.Controller;

import com.neman.orderms.Client.ProductClient;
import com.neman.orderms.Client.UserClient;
import com.neman.orderms.Dto.OrderRequestDto;
import com.neman.orderms.Dto.OrderResponseDto;
import com.neman.orderms.Dto.ProductResponseDto;
import com.neman.orderms.Dto.UserResponseDto;
import com.neman.orderms.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
     private final OrderService orderService;
     private final ProductClient productClient;

     @PostMapping("/create")
     public OrderResponseDto createOrder(@RequestBody OrderRequestDto orderRequestDto) {
         return orderService.createOrder(orderRequestDto);
     }

     @GetMapping("/findAll")
     public List<OrderResponseDto> getAllOrders() {
            return orderService.getAllOrders();
     }

     @GetMapping("/getOrderById/{id}")
     public OrderResponseDto getOrderById(@PathVariable Long id) {
            return orderService.getOrderById(id);
     }



    @GetMapping("/product/{productId}")
    public ProductResponseDto getProductInfo(@PathVariable Long productId) {
        return productClient.getProductById(productId);
    }

    @GetMapping("/user/{userId}")
    public UserResponseDto getUserInfo(@PathVariable Long userId) {
        return orderService.getUserById(userId);
    }
}
