package com.example.ecommerce.controller;

import com.example.ecommerce.entity.Cart;
import org.springframework.web.bind.annotation.*;

@RestController
public class CartController {

    @PostMapping("/cart/{cartId}/products/{productId}")
    Cart addProductToCart(@PathVariable int cartId, @PathVariable int productId) {

    }

}
