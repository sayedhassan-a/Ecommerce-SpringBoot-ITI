package org.example.ecommerce.services;

import org.example.ecommerce.dtos.CartItemRequestDTO;
import org.example.ecommerce.dtos.CartItemResponseDTO;
import org.example.ecommerce.exceptionHandling.exception.BadRequestException;
import org.example.ecommerce.models.CartItem;
import org.example.ecommerce.models.CartKey;
import org.example.ecommerce.models.Customer;
import org.example.ecommerce.models.Product;
import org.example.ecommerce.repositories.CartItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public CartItemResponseDTO setQuantity(Long productId,
                                          Long customerId, int quantity) {
        //TODO Retrieve customer and product
        Customer customer = null;
        Product product = null;

        if(product.getStock() < -1) {
            throw new BadRequestException("Quantity cannot be negative");
        }

        if(product.getStock() < quantity) {
            throw new BadRequestException("Available quantity is not enough");
        }

        CartKey cartKey = new CartKey(customer, product);

        CartItem cartItem = cartItemRepository.findById(cartKey).orElseGet(CartItem::new);
        cartItem.setQuantity(quantity);
        cartItem.setCustomer(customer);
        cartItem.setProduct(product);
        if (quantity == 0) {
            cartItemRepository.delete(cartItem);
            return null;
        }

        CartItem saved = cartItemRepository.save(cartItem);

        CartItemResponseDTO cartItemResponseDTO = new CartItemResponseDTO();
        cartItemResponseDTO.setQuantity(saved.getQuantity());
        //TODO: set product dto
        return cartItemResponseDTO;
    }

    @Transactional
    public boolean emptyCart(Long customerId) {
        cartItemRepository.deleteAll(cartItemRepository.findByCustomerId(customerId));
        return true;
    }
    
    public List<CartItemResponseDTO> findByCustomerId(Long customerId) {
        return cartItemRepository.findByCustomerId(customerId).stream().map((cartItem) -> {
            CartItemResponseDTO cartItemResponseDTO = new CartItemResponseDTO();
            cartItemResponseDTO.setQuantity(cartItem.getQuantity());
            //TODO: put product dto
            return cartItemResponseDTO;
        }).toList();
    }

    public List<CartItem> findCartItemsByCustomerId(Long customerId) {
        return cartItemRepository.findByCustomerId(customerId);
    }
}
