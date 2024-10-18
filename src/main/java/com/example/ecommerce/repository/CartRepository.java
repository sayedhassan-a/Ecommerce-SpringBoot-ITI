package com.example.ecommerce.repository;

import com.example.ecommerce.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    public Cart findCartByCustomerId(int costumerId);

}
