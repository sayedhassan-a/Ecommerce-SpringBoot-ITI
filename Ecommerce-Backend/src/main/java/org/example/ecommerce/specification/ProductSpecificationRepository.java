package org.example.ecommerce.specification;

import jakarta.persistence.criteria.Predicate;
import org.example.ecommerce.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ProductSpecificationRepository {

    Page<ProductSpecification>  filterBySpecifications(Map<String, String> filters, Pageable pageable);

}
