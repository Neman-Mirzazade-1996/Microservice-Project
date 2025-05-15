package com.neman.productms.Dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponseDto {
    Long id;
    String name;
    String description;
    String category;
    String brand;
    String sku;
    Double price;
    Integer stockQuantity;
    String status;
    String imageUrl;
    String createdAt;
    String updatedAt;
}
