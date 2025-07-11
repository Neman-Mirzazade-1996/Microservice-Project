package com.neman.productms.Service.ServiceImpl;

import com.neman.productms.Dto.ProductRequestDto;
import com.neman.productms.Dto.ProductResponseDto;
import com.neman.productms.Dto.ProductUpdateDto;
import com.neman.productms.Exception.CategoryNotFoundException;
import com.neman.productms.Exception.ProductAlreadyExistException;
import com.neman.productms.Exception.ProductNotFoundException;
import com.neman.productms.Mapper.ProductMapper;
import com.neman.productms.Model.Category;
import com.neman.productms.Model.Product;
import com.neman.productms.Repository.CategoryRepository;
import com.neman.productms.Repository.ProductRepository;
import com.neman.productms.Service.ProductService;
import com.neman.productms.Specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponseDto createProduct(ProductRequestDto dto) {
        if (productRepository.existsBySku(dto.getSku())) {
            throw new ProductAlreadyExistException("Product with SKU " + dto.getSku() + " already exists");
        }

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        Product product = Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .category(category)
                .brand(dto.getBrand())
                .sku(dto.getSku())
                .price(dto.getPrice())
                .stockQuantity(1)
                .status(dto.getStatus())
                .imageUrl(dto.getImageUrl())
                .build();
        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    public ProductResponseDto updateProduct(Long id, ProductUpdateDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setCategory(category);
        product.setBrand(dto.getBrand());
        product.setSku(dto.getSku());
        product.setPrice(dto.getPrice());
        product.setStatus(dto.getStatus());
        product.setImageUrl(dto.getImageUrl());
        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        productRepository.delete(product);
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        return productMapper.toDto(product);
    }

    @Override
    public List<ProductResponseDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return productMapper.toDtoList(products);
    }

    @Override
    public void increaseStock(Long productId, int quantity) {
        Product product=productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        product.setStockQuantity(product.getStockQuantity() + quantity);
        productMapper.toDto(productRepository.save(product));
    }

    @Transactional
    @Override
    public void decreaseStock(Long productId, int quantity) {
        Product product=productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Not enough stock");
        }
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productMapper.toDto(productRepository.save(product));
    }

    @Override
    public List<ProductResponseDto> searchProducts(String name, String brand, Double minPrice, Double maxPrice, String status) {
        Specification<Product> spec = where(ProductSpecification.hasName(name))
                .and(ProductSpecification.hasBrand(brand))
                .and(ProductSpecification.priceBetween(minPrice, maxPrice))
                .and(ProductSpecification.hasStatus(status));

        return productMapper.toDtoList(productRepository.findAll(spec));
    }

}
