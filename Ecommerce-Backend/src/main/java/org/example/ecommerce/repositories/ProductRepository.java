package org.example.ecommerce.repositories;

import org.example.ecommerce.models.Product;
import org.example.ecommerce.specification.ProductSpecificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.specsId = ?1")
    Product findBySpecsId(String specsId);
}
