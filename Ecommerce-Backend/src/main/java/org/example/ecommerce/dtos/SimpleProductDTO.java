package org.example.ecommerce.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SimpleProductDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private String image;
    private String categoryName;
    private String brandName;
    private String stock;
    private int salePercentage;
}
