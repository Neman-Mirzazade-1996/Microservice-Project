package com.neman.productms.Controller;


import com.neman.productms.Dto.CategoryRequestDto;
import com.neman.productms.Dto.CategoryResponseDto;
import com.neman.productms.Service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/create")
    public CategoryResponseDto create(CategoryRequestDto dto) {
        return categoryService.createCategory(dto);
    }

    @PutMapping("/update/{id}")
    public CategoryResponseDto update(@PathVariable Long id, CategoryRequestDto dto) {
        return categoryService.updateCategory(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }

    @GetMapping("/{id}")
    public CategoryResponseDto getById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping("/all")
    public List<CategoryResponseDto> getAll() {
        return categoryService.getAllCategories();
    }
}
