package com.neman.productms.Mapper;

import com.neman.productms.Dto.CategoryRequestDto;
import com.neman.productms.Dto.CategoryResponseDto;
import com.neman.productms.Model.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CategoryRequestDto dto);
    CategoryResponseDto toDto(Category category);
    List<CategoryResponseDto> toDtoList(List<Category> categories);
}
