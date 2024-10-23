package org.example.ecommerce.controllers;

import jakarta.validation.Valid;
import org.example.ecommerce.dtos.CategoryDTO;
import org.example.ecommerce.dtos.SubCategoryDTO;
import org.example.ecommerce.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories/strings")
    public String getString(){
        return "yes";
    }

    @GetMapping("/categories")
    public List<CategoryDTO> getAllCategories(){
        return categoryService.getAllCategories();
    }
    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryDTO> getSubCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.get(id));
    }
    @PostMapping("/categories")
    public ResponseEntity<CategoryDTO> createProduct(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO addedCategory = categoryService.add(categoryDTO);
        return new ResponseEntity<>(addedCategory, HttpStatus.CREATED);
    }
}
