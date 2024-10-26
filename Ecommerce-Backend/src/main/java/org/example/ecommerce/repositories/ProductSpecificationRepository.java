package org.example.ecommerce.repositories;

import org.example.ecommerce.specifications.CustomProductSpecificationRepository;
import org.example.ecommerce.specifications.ProductSpecs;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public interface ProductSpecificationRepository extends MongoRepository<ProductSpecs, String>, CustomProductSpecificationRepository {


}

