package com.example.ecommerce.dto;

import com.example.ecommerce.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDTO {
    private Integer id;
    private String categoryName;

    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.categoryName = category.getName();
    }
}
