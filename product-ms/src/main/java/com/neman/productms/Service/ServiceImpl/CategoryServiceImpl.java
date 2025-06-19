package com.neman.productms.Service.ServiceImpl;

import com.neman.productms.Dto.CategoryRequestDto;
import com.neman.productms.Dto.CategoryResponseDto;
import com.neman.productms.Exception.CategoryAlreadyExistException;
import com.neman.productms.Exception.CategoryNotFoundException;
import com.neman.productms.Mapper.CategoryMapper;
import com.neman.productms.Model.Category;
import com.neman.productms.Repository.CategoryRepository;
import com.neman.productms.Service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto dto) {
        if (categoryRepository.existsByName(dto.getName())) {
            throw new CategoryAlreadyExistException("Category with name " + dto.getName() + " already exists");
        }

        Category category = Category.builder()
                .name(dto.getName())
                .build();

        return categoryMapper.toDto(categoryRepository.save(category));

    }

    @Override
    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));


        category.setName(dto.getName());
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        categoryRepository.delete(category);
    }

    @Override
    public CategoryResponseDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        return categoryMapper.toDto(category);
    }

    @Override
    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new CategoryNotFoundException("No categories found");
        }
        return categories.stream()
                .map(categoryMapper::toDto)
                .toList();
    }
}
