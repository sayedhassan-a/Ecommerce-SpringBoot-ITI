package org.example.ecommerce.specifications;

import java.util.List;
import java.util.Map;

public interface CustomProductSpecificationRepository {
    List<ProductSpecs> findByDynamicFilters(Map<String, List<String>> filters);

}
