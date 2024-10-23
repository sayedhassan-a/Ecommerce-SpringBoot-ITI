package org.example.ecommerce.mapper;

import org.example.ecommerce.dto.CategoryDTO;
import org.example.ecommerce.models.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends GenericMapper<Category, CategoryDTO> {
}
