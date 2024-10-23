package org.example.ecommerce.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private Set<SubCategoryDTO> subCategories;
}
