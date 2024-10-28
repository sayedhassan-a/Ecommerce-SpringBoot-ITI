package org.example.ecommerce.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductDTO {
    private ProductRequestDTO productDto;
    private ProductSpecsDTO productSpecsDTO;
}

