package com.neman.orderms.Mapper;

import com.neman.orderms.Dto.OrderRequestDto;
import com.neman.orderms.Dto.OrderResponseDto;
import com.neman.orderms.Model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponseDto toDto(Order order);
    Order toEntity(OrderRequestDto dto);
}
