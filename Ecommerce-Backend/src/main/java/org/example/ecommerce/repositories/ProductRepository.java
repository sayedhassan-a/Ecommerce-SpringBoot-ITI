package org.example.ecommerce.repositories;

import org.example.ecommerce.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.specsId = ?1")
    Product findBySpecsId(String specsId);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Product> searchProductsByName(@Param("keyword") String keyword, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseAndPriceBetween(
            String name, int minPrice, int maxPrice, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE "
            + "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND "
            + "(:minPrice IS NULL OR p.price >= :minPrice) AND "
            + "(:maxPrice IS NULL OR p.price <= :maxPrice) AND "
            + "(:category IS NULL OR p.subCategory.category.name = :category) AND "
            + "(:subCategory IS NULL OR p.subCategory.name = :subCategory)")
    Page<Product> searchProducts(
            @Param("name") String name,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("category") String category,
            @Param("subCategory") String subCategory,
            Pageable pageable);
}
