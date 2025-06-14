package com.neman.productms.Mapper;


import com.neman.productms.Dto.ProductRequestDto;
import com.neman.productms.Dto.ProductResponseDto;
import com.neman.productms.Model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponseDto toDto(Product product);
    List<ProductResponseDto> toDtoList(List<Product> products);
}
