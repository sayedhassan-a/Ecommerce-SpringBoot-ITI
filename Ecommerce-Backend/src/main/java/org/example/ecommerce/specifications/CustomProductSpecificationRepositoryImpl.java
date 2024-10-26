package org.example.ecommerce.specifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class CustomProductSpecificationRepositoryImpl implements CustomProductSpecificationRepository{
    private final MongoTemplate mongoTemplate;

    @Autowired
    public CustomProductSpecificationRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    @Override
    public List<ProductSpecs> findByDynamicFilters(Map<String, List<String>> filters) {
        List<Criteria> criteriaList = new ArrayList<>();

        // Normalize all keys in filters to lowercase
        filters.forEach((key, values) -> {
            String normalizedKey = key.toLowerCase();  // Convert key to lowercase

            // Use a case-insensitive regex pattern for each value in the list
            List<Pattern> regexPatterns = values.stream()
                    .map(value -> Pattern.compile(Pattern.quote(value), Pattern.CASE_INSENSITIVE))
                    .collect(Collectors.toList());

            criteriaList.add(Criteria.where("key").regex(Pattern.compile("^" + normalizedKey + "$", Pattern.CASE_INSENSITIVE))
                    .and("value").in(regexPatterns));
        });

        // Combines all criteria with an AND operation
        Criteria finalCriteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));
        Query query = new Query(finalCriteria);

        return mongoTemplate.find(query, ProductSpecs.class);
    }
}
