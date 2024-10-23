package org.example.ecommerce.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ecommerce.models.Category;

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
