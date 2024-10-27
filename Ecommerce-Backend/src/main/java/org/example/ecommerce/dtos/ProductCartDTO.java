package org.example.ecommerce.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCartDTO {
    private Long id;
    private String name;
    private String image;
    private int price;
    private int quantity;
}
