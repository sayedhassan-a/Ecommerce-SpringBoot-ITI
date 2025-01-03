package org.example.ecommerce.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SubCategoryWithSpecificationDTO {
    private SubCategoryDTO subCategory;
    private SubCategorySpecificationDTO subCategorySpecification;
}
