package org.example.ecommerce.dto;

import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ecommerce.models.SubCategory;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private Set<SubCategoryDTO> subCategories;
}
