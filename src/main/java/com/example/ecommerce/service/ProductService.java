package com.example.ecommerce.service;

import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductService {

    Product save(Product product);
    Product update(Product product);
    Optional<ProductDTO> getProductById(Integer id);
    Page<Product> getAllProducts(Map<String, Object> filter, int page, int size);
    boolean deleteProduct(Integer id);
    Product addWithImages(Product product, List<String> images);
    Product updateWithImages(Product product, List<String> images);
}
