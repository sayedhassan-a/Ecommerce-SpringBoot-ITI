package org.example.ecommerce.controllers;

import org.example.ecommerce.dtos.CartItemRequestDTO;
import org.example.ecommerce.dtos.CartItemResponseDTO;
import org.example.ecommerce.services.CartItemService;
import org.example.ecommerce.services.MyUserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {
    private final CartItemService cartItemService;

    public CartController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    ResponseEntity<List<CartItemResponseDTO>> getCartItems() {
        return ResponseEntity.ok(cartItemService.findCartForCurrentUser());
    }

    @PostMapping
    ResponseEntity<CartItemResponseDTO> addCartItem(@RequestBody CartItemRequestDTO cartItemRequestDTO) {
        return ResponseEntity.ok(cartItemService.setQuantity(
                cartItemRequestDTO.getProductId(),
                cartItemRequestDTO.getQuantity()));
    }

    @PutMapping
    ResponseEntity<List<CartItemResponseDTO>> merge(@RequestBody List<CartItemRequestDTO> cartItemRequestDTOs) {
        return ResponseEntity.ok(cartItemService.mergeCart(cartItemRequestDTOs));
    }

}
