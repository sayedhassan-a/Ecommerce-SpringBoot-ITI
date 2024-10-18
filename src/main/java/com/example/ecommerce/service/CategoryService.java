package com.example.ecommerce.service;

import com.example.ecommerce.entity.Category;

import java.util.List;

public interface CategoryService {
    Category addCategory(Category category);
    Category updateCategory(Category category);
    void deleteCategory(Category category);
    List<Category> getCategories(int page, int size);
    Category getCategoryById(int id);


}
