package org.example.ecommerce.repositories;

import org.example.ecommerce.models.CartItem;
import org.example.ecommerce.models.CartKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, CartKey> {
    List<CartItem> findByCustomerId(Long id);
    List<CartItem> findByCustomerEmail(String email);
}
