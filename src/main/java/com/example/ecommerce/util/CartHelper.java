package com.example.ecommerce.util;

import com.example.ecommerce.entity.*;
import com.example.ecommerce.repository.CartHasProductRepository;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.repository.ProductRepository;

import org.springframework.stereotype.Service;

@Service
public class CartHelper {
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CartHasProductRepository cartHasProductRepository;


    public CartHelper(CustomerRepository customerRepository, ProductRepository productRepository, CartHasProductRepository cartHasProductRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.cartHasProductRepository = cartHasProductRepository;
    }

    public CartHasProduct createCartProduct(Cart cart, Product product){
        CartHasProductID chpId = new CartHasProductID();
        chpId.setCartId(cart.getId());
        chpId.setProductId(product.getId());
        CartHasProduct chp = new CartHasProduct();
        chp.setCartHasProductID(chpId);
        chp.setCart(cart);
        chp.setProduct(product);
        chp.setQuantity(1);
        return chp;
    }

    public void saveItem(int customerId, int productId){
        Customer customer = customerRepository.findById(customerId).orElse(null);
        Product product = productRepository.findById(productId).orElse(null);
        CartHasProduct cartHasProduct = createCartProduct(customer.getCart(),product);
        cartHasProductRepository.save(cartHasProduct);
    }

    public void removeItem(int customerId, int productId){
        Customer customer = customerRepository.findById(customerId).orElse(null);
        try {
            cartHasProductRepository.deleteByCartIdAndProductId(customer.getCart().getId(),productId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
