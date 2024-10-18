package com.example.ecommerce.repository;

import com.example.ecommerce.entity.CartHasProduct;
import com.example.ecommerce.entity.CartHasProductID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface CartHasProductRepository extends JpaRepository<CartHasProduct, CartHasProductID> {

    void deleteByCartIdAndProductId(int cartId, int productId);
    Optional<CartHasProduct> findByCartCustomerIdAndProductId(int customerId, int productId);
}
