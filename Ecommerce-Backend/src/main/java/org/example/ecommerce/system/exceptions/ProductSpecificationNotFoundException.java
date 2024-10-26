package org.example.ecommerce.system.exceptions;

public class ProductSpecificationNotFoundException extends RuntimeException {
    public ProductSpecificationNotFoundException(String id) {
        super("Specs with ID " + id + " not found.");
    }
}
