package org.example.ecommerce.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemResponseDTO {
    private ProductCartDTO product;
    private int quantity;
}
