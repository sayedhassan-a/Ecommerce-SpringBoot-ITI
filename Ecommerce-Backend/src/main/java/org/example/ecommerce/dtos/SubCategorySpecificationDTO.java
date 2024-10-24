package org.example.ecommerce.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SubCategorySpecificationDTO {
    private String id;
    private String name;
    private List<SpecsDTO> specs;
}
