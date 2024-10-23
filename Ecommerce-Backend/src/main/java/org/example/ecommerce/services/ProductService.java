package org.example.ecommerce.services;

import org.example.ecommerce.models.Product;
import org.example.ecommerce.repositories.ProductRepository;
import org.example.ecommerce.specification.ProductSpecification;
import org.example.ecommerce.specification.ProductSpecificationRepository;
import org.example.ecommerce.specification.productSpecificationRepositoryImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductSpecificationRepository productSpecificationRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductSpecificationRepository productSpecificationRepository, ProductRepository productRepository, productSpecificationRepositoryImplementation productSpecificationRepositoryImplementation) {
        this.productSpecificationRepository = productSpecificationRepository;
        this.productRepository = productRepository;
    }


    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id).map(product -> {
            product.setName(productDetails.getName());
            product.setPrice(productDetails.getPrice());
            product.setDescription(productDetails.getDescription());
            product.setStock(productDetails.getStock());
            product.setBrandName(productDetails.getBrandName());
            product.setImage(productDetails.getImage());

            // Optionally validate and update specsId here
            return productRepository.save(product);
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void deleteProduct(Long id) {
        productRepository.findById(id).ifPresent(product -> {
            product.setDeleted(true);
            productRepository.save(product);
        });
    }

    public Page<Product> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable);
    }




}
