package com.neman.productms.Service;

import com.neman.productms.Dto.ProductRequestDto;
import com.neman.productms.Dto.ProductResponseDto;
import com.neman.productms.Dto.ProductUpdateDto;

import java.util.List;

public interface ProductService {
    ProductResponseDto createProduct(ProductRequestDto dto);
    ProductResponseDto updateProduct(Long id, ProductUpdateDto dto);
    void deleteProduct(Long id);
    ProductResponseDto getProductById(Long id);
    List<ProductResponseDto> getAllProducts();

    void increaseStock(Long productId, int quantity);
    void decreaseStock(Long productId, int quantity);
}
