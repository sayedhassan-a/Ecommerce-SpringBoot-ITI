package org.example.ecommerce.controllers;

import org.example.ecommerce.dtos.CartItemRequestDTO;
import org.example.ecommerce.dtos.CartItemResponseDTO;
import org.example.ecommerce.services.CartItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {
    private final CartItemService cartItemService;

    public CartController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    List<CartItemResponseDTO> getCartItems() {
        //TODO Retrieve Current CustomerID
        Long customerId = null;
        return cartItemService.findByCustomerId(customerId);
    }

    @PostMapping
    CartItemResponseDTO addCartItem(@RequestBody CartItemRequestDTO cartItemRequestDTO) {
        //TODO: retrieve current customer id
        Long customerId = null;
        return cartItemService.setQuantity(customerId,
                cartItemRequestDTO.getProductId(), cartItemRequestDTO.getQuantity());
    }

}
