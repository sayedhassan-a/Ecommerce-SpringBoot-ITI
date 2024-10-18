package com.example.ecommerce.repository;

import com.example.ecommerce.entity.ProductSpecs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ProductSpecsRepository extends JpaRepository<ProductSpecs, Integer> {}
