package org.example.ecommerce.repositories;

import org.example.ecommerce.models.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubCategoryRepository extends JpaRepository<SubCategory,Long> {
    @Query("select q from SubCategory q where q.id=?1")
    SubCategory findBy(Long id);
}
