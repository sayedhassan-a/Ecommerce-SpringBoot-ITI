package org.example.ecommerce.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecommerce.models.Image;
import org.example.ecommerce.specification.ProductSpecs;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
   private Long id;
   private String name;
   private BigDecimal price;
   private String description;
   private String image;
   private ProductSpecs productSpecs;
}
