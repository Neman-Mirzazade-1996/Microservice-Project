package com.neman.productms.Controller;

import com.neman.productms.Dto.ProductRequestDto;
import com.neman.productms.Dto.ProductResponseDto;
import com.neman.productms.Dto.ProductUpdateDto;
import com.neman.productms.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/create")
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto dto) {
        return productService.createProduct(dto);
    }

    @PutMapping("/update/{id}")
    public ProductResponseDto updateProduct(@PathVariable Long id, @RequestBody ProductUpdateDto dto) {
        return productService.updateProduct(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @GetMapping("/get/{id}")
    public ProductResponseDto getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/getAll")
    public List<ProductResponseDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("/increaseStock/{productId}/{quantity}")
    public void increaseStock(@PathVariable Long productId, @PathVariable int quantity) {
        productService.increaseStock(productId, quantity);
    }

    @PostMapping("/decreaseStock/{productId}/{quantity}")
    public void decreaseStock(@PathVariable Long productId, @PathVariable int quantity) {
        productService.decreaseStock(productId, quantity);
    }
}
