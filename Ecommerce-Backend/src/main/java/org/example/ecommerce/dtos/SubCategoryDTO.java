package org.example.ecommerce.dtos;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SubCategoryDTO {
    private Long id;
    private String name;
    private String structureId;
    private SubCategorySpecificationDTO specification;
    private String categoryName;
}
