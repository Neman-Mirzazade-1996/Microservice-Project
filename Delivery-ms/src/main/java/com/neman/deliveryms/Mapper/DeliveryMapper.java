package com.neman.deliveryms.Mapper;

import com.neman.deliveryms.Dto.DeliveryDto;
import com.neman.deliveryms.Model.Delivery;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {
     Delivery toEntity(DeliveryDto dto);
     DeliveryDto toDto(Delivery delivery);
}
