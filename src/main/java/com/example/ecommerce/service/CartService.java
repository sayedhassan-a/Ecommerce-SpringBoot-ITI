package com.example.ecommerce.service;

import com.example.ecommerce.dto.ItemDTO;
import com.example.ecommerce.repository.CartHasProductRepository;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.util.CartHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository ;
    private final CartHasProductRepository cartHasProductRepository ;
    private final CartHelper cartHelper;

    public CartService(CartRepository cartRepository, CartHasProductRepository cartHasProductRepository, CartHelper cartHelper) {
        this.cartRepository = cartRepository;
        this.cartHasProductRepository = cartHasProductRepository;
        this.cartHelper = cartHelper;
    }

    public void addCartItem(int customerId, int productId){
        cartHelper.saveItem(customerId,productId);
    }

    public void removeCartItem(int customerId, int productId){
        cartHelper.removeItem(customerId,productId);
    }

    public List<ItemDTO> getCartItems(int customerId){
        return cartRepository.findCartByCustomerId(customerId).getCartHasProducts()
                .stream()
                .map(ItemDTO::new)
                .collect(Collectors.toList());
    }
    public void emptyCart(int customerId){
        cartHasProductRepository
                .deleteAll(
                        cartRepository
                        .findCartByCustomerId(customerId)
                        .getCartHasProducts());
    }
    public ItemDTO getItem(int customerId, int itemId){
        return cartHasProductRepository.findByCartCustomerIdAndProductId(customerId,itemId).isEmpty() ? null :
                new ItemDTO(cartHasProductRepository.findByCartCustomerIdAndProductId(customerId,itemId).get());
    }

}
