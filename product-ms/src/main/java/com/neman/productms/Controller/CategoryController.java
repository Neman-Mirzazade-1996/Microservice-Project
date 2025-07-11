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

    @PostMapping("/admin/create")
    public CategoryResponseDto create(@RequestBody CategoryRequestDto dto) {
        return categoryService.createCategory(dto);
    }

    @PutMapping("/admin/update/{id}")
    public CategoryResponseDto update(@PathVariable Long id, @RequestBody CategoryRequestDto dto) {
        return categoryService.updateCategory(id, dto);
    }

    @DeleteMapping("/admin/delete/{id}")
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
