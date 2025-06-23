package com.neman.orderms.Dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponseDto {
    Long id;
    String name;
    String description;
    CategoryDto category;  // Changed from String to CategoryDto
    String brand;
    String sku;
    Double price;
    Integer stockQuantity;
    String status;
    String imageUrl;
    String createdAt;
    String updatedAt;
}
