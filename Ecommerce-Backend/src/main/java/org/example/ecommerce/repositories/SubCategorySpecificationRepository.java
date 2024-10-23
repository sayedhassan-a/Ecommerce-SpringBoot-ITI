package org.example.ecommerce.repositories;

import org.example.ecommerce.models.SubCategorySpecification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubCategorySpecificationRepository extends MongoRepository<SubCategorySpecification,String> {

}
