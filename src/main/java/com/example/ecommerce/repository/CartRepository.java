package com.example.ecommerce.repository;

import com.example.ecommerce.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CartRepository extends JpaRepository<Cart, Integer> {

    public Cart findCartByCustomerId(int costumerId);

}
