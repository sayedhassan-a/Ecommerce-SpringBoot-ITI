package com.example.ecommerce.dto;


import com.example.ecommerce.entity.CartHasProduct;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ItemDTO {
    private Integer id;
    private String name;
    private int price;
    private String image;
    private int quantity;

    public ItemDTO(CartHasProduct item){
        this.quantity = item.getQuantity();
        this.id = item.getProduct().getId();
        this.name = item.getProduct().getName();
        this.price = item.getProduct().getPrice();
        this.image = item.getProduct().getImage();
    }

}
