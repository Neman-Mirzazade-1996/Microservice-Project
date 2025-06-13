package com.neman.productms.Service;

import com.neman.productms.Dto.CategoryRequestDto;
import com.neman.productms.Dto.CategoryResponseDto;

import java.util.List;

public interface CategoryService {

    CategoryResponseDto createCategory(CategoryRequestDto dto);
    CategoryResponseDto updateCategory(Long id, CategoryRequestDto dto);
    void deleteCategory(Long id);
    CategoryResponseDto getCategoryById(Long id);
     List<CategoryResponseDto> getAllCategories();
}
