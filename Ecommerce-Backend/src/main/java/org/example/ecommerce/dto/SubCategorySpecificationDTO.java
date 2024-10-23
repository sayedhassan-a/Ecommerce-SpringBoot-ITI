package org.example.ecommerce.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ecommerce.models.Specs;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SubCategorySpecificationDTO {
    private String id;
    private String name;
    private List<SpecsDTO> specs;
}
