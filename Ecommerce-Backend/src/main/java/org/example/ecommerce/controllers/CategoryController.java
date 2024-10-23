package org.example.ecommerce.controllers;

import org.example.ecommerce.dtos.CategoryDTO;
import org.example.ecommerce.dtos.SubCategoryDTO;
import org.example.ecommerce.services.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<CategoryDTO>> getSubCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.get(id));
    }
}
