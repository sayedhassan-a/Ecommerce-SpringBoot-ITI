package org.example.ecommerce.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductWithSpecsDTO {
    private ProductRequestDTO productDto;
    private ProductSpecsDTO productSpecsDTO;
}
