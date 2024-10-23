package org.example.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductWithSpecsDTO {
    private ProductDTO productDto;
    private ProductSpecsDTO productSpecsDTO;
}
