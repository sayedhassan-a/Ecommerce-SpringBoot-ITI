package org.example.ecommerce.repositories;

import org.example.ecommerce.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {

     List<Category> findAllBy();
     Category findByName(String name);
}
