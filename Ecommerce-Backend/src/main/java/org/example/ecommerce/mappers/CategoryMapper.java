package org.example.ecommerce.mappers;

import org.example.ecommerce.dtos.CategoryDTO;
import org.example.ecommerce.models.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends GenericMapper<Category, CategoryDTO> {
        List<CategoryDTO> toDTOList(List<Category> categories);
}
