package com.neman.deliveryms.Service.ServiceImpl;

import com.neman.deliveryms.Dto.DeliveryDto;
import com.neman.deliveryms.Mapper.DeliveryMapper;
import com.neman.deliveryms.Model.Delivery;
import com.neman.deliveryms.Repository.DeliveryRepository;
import com.neman.deliveryms.Service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    @Override
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        Delivery delivery= Delivery.builder()
                .orderId(deliveryDto.getOrderId())
                .trackingNumber(deliveryDto.getTrackingNumber())
                .deliveryAddress(deliveryDto.getDeliveryAddress())
                .deliveryStatus(deliveryDto.getDeliveryStatus())
                .carrier(deliveryDto.getCarrier())
                .expectedDate(deliveryDto.getExpectedDate())
                .shippedDate(deliveryDto.getShippedDate())
                .deliveredDate(deliveryDto.getDeliveredDate())
                .recipientName(deliveryDto.getRecipientName())
                .recipientPhone(deliveryDto.getRecipientPhone())
                .build();
        log.info("Creating new delivery with order ID: {}", deliveryDto.getOrderId());
        Delivery savedDelivery = deliveryRepository.save(delivery);
        return deliveryMapper.toDto(savedDelivery);
    }
}
