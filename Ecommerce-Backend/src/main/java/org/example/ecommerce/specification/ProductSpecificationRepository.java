package org.example.ecommerce.specification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Map;

public interface ProductSpecificationRepository extends MongoRepository<ProductSpecs, String> {

}
