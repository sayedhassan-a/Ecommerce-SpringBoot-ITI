package org.example.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecommerce.models.Image;
import org.example.ecommerce.specification.ProductSpecification;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO{
   private Long id;
   private String name;
   private BigDecimal price;
   private String description;
   private List<Image> productImages;
}
