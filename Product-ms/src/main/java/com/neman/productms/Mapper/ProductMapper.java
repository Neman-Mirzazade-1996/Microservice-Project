package com.neman.productms.Mapper;


import com.neman.productms.Dto.ProductRequestDto;
import com.neman.productms.Dto.ProductResponseDto;
import com.neman.productms.Model.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductRequestDto dto);

    ProductResponseDto toDto(Product product);

    List<ProductResponseDto> toDtoList(List<Product> products);
}
