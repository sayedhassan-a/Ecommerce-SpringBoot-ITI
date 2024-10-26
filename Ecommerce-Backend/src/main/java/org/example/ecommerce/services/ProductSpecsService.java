package org.example.ecommerce.services;

import org.example.ecommerce.specifications.ProductSpecs;
import org.example.ecommerce.repositories.ProductSpecificationRepository;
import org.example.ecommerce.system.exceptions.ProductSpecificationNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSpecsService {

    private final ProductSpecificationRepository productSpecificationRepository;

    public ProductSpecsService(ProductSpecificationRepository productSpecificationRepository) {
        this.productSpecificationRepository = productSpecificationRepository;
    }

    public ProductSpecs saveProductSpecification(ProductSpecs spec) {
        return productSpecificationRepository.save(spec);
    }


    public ProductSpecs getProductSpecificationById(String id) {
        return productSpecificationRepository.findById(id)
                .orElseThrow(() -> new ProductSpecificationNotFoundException(id));
    }

    public List<ProductSpecs> getAllProductSpecifications() {
        return productSpecificationRepository.findAll();
    }

    public ProductSpecs updateProductSpecification(String id, ProductSpecs specDetails) {
        return productSpecificationRepository.findById(id)
                .map(existingSpec -> {
                    existingSpec.setProductId(specDetails.getProductId());
                    existingSpec.setKey(specDetails.getKey());
                    existingSpec.setValue(specDetails.getValue());
                    return productSpecificationRepository.save(existingSpec);
                }).orElseThrow(() -> new ProductSpecificationNotFoundException(id));
    }

    public void deleteProductSpecification(String id) {
        ProductSpecs spec = productSpecificationRepository.findById(id)
                .orElseThrow(() -> new ProductSpecificationNotFoundException(id));
        productSpecificationRepository.delete(spec);
    }
}
