package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {

    List<Product> findByCategoryName(String name);
    boolean deleteById(int id);
}
