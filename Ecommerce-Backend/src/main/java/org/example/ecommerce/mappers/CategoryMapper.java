package org.example.ecommerce.mappers;

import org.example.ecommerce.dtos.CategoryDTO;
import org.example.ecommerce.models.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends GenericMapper<Category, CategoryDTO> {
}
