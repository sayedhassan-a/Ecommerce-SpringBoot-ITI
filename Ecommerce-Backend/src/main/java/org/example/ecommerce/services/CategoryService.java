package org.example.ecommerce.services;

import org.example.ecommerce.dto.CategoryDTO;
import org.example.ecommerce.mapper.CategoryMapper;
import org.example.ecommerce.models.Category;
import org.example.ecommerce.repositories.CategoryRepository;
import org.example.ecommerce.repositories.SubCategorySpecificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public Optional<CategoryDTO> get(Long id){
        Optional<Category> category = categoryRepository.findById(id);
        return Optional.ofNullable(categoryMapper.toDTO(category.get()));
    }



}
