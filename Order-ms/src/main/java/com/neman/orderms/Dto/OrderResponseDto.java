package com.neman.orderms.Dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponseDto {
    Long id;
    Long userId;
    Long productId;
    Integer quantity;
    Double totalPrice;
    String status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
