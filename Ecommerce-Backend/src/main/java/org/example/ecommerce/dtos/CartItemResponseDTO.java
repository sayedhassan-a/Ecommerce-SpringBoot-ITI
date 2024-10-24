package org.example.ecommerce.dtos;

import lombok.Getter;
import lombok.Setter;
import org.example.ecommerce.models.Product;

@Getter
@Setter
public class CartItemResponseDTO {
    //TODO: put product DTO
    //private Product product;
    private int quantity;
}
