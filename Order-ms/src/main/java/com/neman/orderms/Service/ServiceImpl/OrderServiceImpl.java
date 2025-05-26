package com.neman.orderms.Service.ServiceImpl;

import com.neman.orderms.Client.ProductClient;
import com.neman.orderms.Client.UserClient;
import com.neman.orderms.Dto.OrderRequestDto;
import com.neman.orderms.Dto.OrderResponseDto;
import com.neman.orderms.Dto.ProductResponseDto;
import com.neman.orderms.Dto.UserResponseDto;
import com.neman.orderms.Exception.InsufficientStockException;
import com.neman.orderms.Exception.OrderNotFoundException;
import com.neman.orderms.Mapper.OrderMapper;
import com.neman.orderms.Model.Orders;
import com.neman.orderms.Repository.OrderRepository;
import com.neman.orderms.Service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductClient productClient;
    private final UserClient userClient;
    @Override
    public OrderResponseDto createOrder(OrderRequestDto dto) {
        ProductResponseDto product= productClient.getProductById(dto.getProductId());
        if (product.getStockQuantity() < dto.getQuantity()) {
            throw new InsufficientStockException("Insufficient stock for product with ID: " + dto.getProductId());
        }

        double totalPrice = product.getPrice() * dto.getQuantity();

        productClient.decreaseStock(dto.getProductId(), dto.getQuantity());

        Orders orders = Orders.builder()
                .userId(dto.getUserId())
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .totalPrice(totalPrice)
                .status("PENDING")
                .build();
        return orderMapper.toDto(orderRepository.save(orders));
    }

    @Override
    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderResponseDto getOrderById(Long id) {
        Orders orders =orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id));
        return orderMapper.toDto(orders);
    }

    @Override
    public UserResponseDto getUserById(Long userId) {
    UserResponseDto userResponseDto = userClient.getUserById(userId);
        if (userResponseDto == null) {
            throw new OrderNotFoundException("User not found with ID: " + userId);
        }
        return userResponseDto;
    }


}
