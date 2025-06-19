package com.neman.productms.Dto;

import com.neman.productms.Model.Category;
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
    CategoryResponseDto category;
    String brand;
    String sku;
    Double price;
    Integer stockQuantity;
    String status;
    String imageUrl;
    String createdAt;
    String updatedAt;
}
