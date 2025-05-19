package com.neman.orderms.Service;

import com.neman.orderms.Dto.OrderRequestDto;
import com.neman.orderms.Dto.OrderResponseDto;

import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto dto);
    List<OrderResponseDto> getAllOrders();
    OrderResponseDto getOrderById(Long id);
}
