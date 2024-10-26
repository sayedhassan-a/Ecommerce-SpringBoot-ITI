package org.example.ecommerce.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSpecsDTO {
    private String id;
    private String productId;
    private List<String> key;
    private List<String> value;
}
