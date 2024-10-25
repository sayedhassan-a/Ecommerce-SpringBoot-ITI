package org.example.ecommerce.specification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.core.query.Query;
import java.util.List;
import java.util.Map;

@Repository
public class productSpecificationRepositoryImplementation {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public productSpecificationRepositoryImplementation(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Page<ProductSpecs> filterBySpecifications(Map<String, String> filters, Pageable pageable) {
        Query query = new Query();
        filters.forEach((key, value) -> {
            query.addCriteria(
                    Criteria.where("key").is(key).and("value").is(value)
            );
        });


        query.with(pageable);


        List<ProductSpecs> filteredResults = mongoTemplate.find(query, ProductSpecs.class);

        long total = mongoTemplate.count(query.skip(-1).limit(-1), ProductSpecs.class);

        return new PageImpl<>(filteredResults, pageable, total);
    }
    }
