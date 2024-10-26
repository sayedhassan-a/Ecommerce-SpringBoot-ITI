package org.example.ecommerce.repositories;

import org.example.ecommerce.specifications.ProductSpecs;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;


public interface ProductSpecificationRepository extends MongoRepository<ProductSpecs, String> {

    @Query("{ $and: [ { 'key': { $in: ?0 } }, { 'value': { $in: ?1 } } ] }")
    List<ProductSpecs> findByDynamicFilters(List<String> keys, List<String> values);


}

