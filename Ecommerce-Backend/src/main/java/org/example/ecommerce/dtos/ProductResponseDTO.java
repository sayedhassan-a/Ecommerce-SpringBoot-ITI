package org.example.ecommerce.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecommerce.models.Image;
import org.example.ecommerce.specifications.ProductSpecs;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
   private Long id;
   private String name;
   private BigDecimal price;
   private String description;
   private String image;
   private String categoryName;
   private Long subCategoryId;
   private String brandName;
   private String stock;
   private int salePercentage;
   private Set<Image> productImages=new HashSet<>();
   private ProductSpecs productSpecs;
}
