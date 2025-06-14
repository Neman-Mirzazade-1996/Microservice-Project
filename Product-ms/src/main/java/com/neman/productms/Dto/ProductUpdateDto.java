package com.neman.productms.Dto;

import com.neman.productms.Model.Category;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateDto {
    String name;
    String description;
    Long categoryId;
    String brand;
    String sku;
    Double price;
    String status;
    String imageUrl;
}
