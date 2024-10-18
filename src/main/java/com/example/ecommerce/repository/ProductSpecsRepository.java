package com.example.ecommerce.repository;

import com.example.ecommerce.entity.ProductSpecs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductSpecsRepository extends JpaRepository<ProductSpecs, Integer> {}
