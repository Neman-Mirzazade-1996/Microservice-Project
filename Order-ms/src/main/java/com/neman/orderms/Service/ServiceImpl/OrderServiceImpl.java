package com.neman.orderms.Service.ServiceImpl;

import com.neman.orderms.Client.ProductClient;
import com.neman.orderms.Dto.OrderRequestDto;
import com.neman.orderms.Dto.OrderResponseDto;
import com.neman.orderms.Dto.ProductResponseDto;
import com.neman.orderms.Exception.InsufficientStockException;
import com.neman.orderms.Exception.OrderNotFoundException;
import com.neman.orderms.Mapper.OrderMapper;
import com.neman.orderms.Model.Order;
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
    @Override
    public OrderResponseDto createOrder(OrderRequestDto dto) {
        ProductResponseDto product= productClient.getProductById(dto.getProductId());
        if (product.getStockQuantity() < dto.getQuantity()) {
            throw new InsufficientStockException("Insufficient stock for product with ID: " + dto.getProductId());
        }

        double totalPrice = product.getPrice() * dto.getQuantity();

        productClient.decreaseStock(dto.getProductId(), dto.getQuantity());

        Order order=Order.builder()
                .userId(dto.getUserId())
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .totalPrice(totalPrice)
                .status("PENDING")
                .build();
        return orderMapper.toDto(orderRepository.save(order));
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
        Order order=orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id));
        return orderMapper.toDto(order);
    }
}
