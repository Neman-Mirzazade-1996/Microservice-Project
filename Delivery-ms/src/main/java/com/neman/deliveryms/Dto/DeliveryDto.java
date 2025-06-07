package com.neman.deliveryms.Dto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryDto {
    Long orderId;
    String trackingNumber;
    String deliveryAddress;
    String deliveryStatus;
    String carrier;
    LocalDate expectedDate;
    LocalDate shippedDate;
    LocalDate deliveredDate;
    String recipientName;
    String recipientPhone;
}
