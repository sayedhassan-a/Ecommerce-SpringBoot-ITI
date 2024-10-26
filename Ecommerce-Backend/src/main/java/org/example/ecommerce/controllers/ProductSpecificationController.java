package org.example.ecommerce.controllers;


import org.example.ecommerce.services.ProductSpecsService;
import org.example.ecommerce.specifications.ProductSpecs;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-specification")
public class ProductSpecificationController {

    private final ProductSpecsService productSpecsService;

    public ProductSpecificationController(ProductSpecsService productSpecsService) {
        this.productSpecsService = productSpecsService;
    }

    @PostMapping
    public ResponseEntity<ProductSpecs> saveProductSpecification(@RequestBody ProductSpecs spec) {
        ProductSpecs savedSpec = productSpecsService.saveProductSpecification(spec);
        return new ResponseEntity<>(savedSpec, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductSpecs> getProductSpecificationById(@PathVariable String id) {
        ProductSpecs spec = productSpecsService.getProductSpecificationById(id);
        return ResponseEntity.ok(spec);
    }

    @GetMapping
    public ResponseEntity<List<ProductSpecs>> getAllProductSpecifications() {
        List<ProductSpecs> specs = productSpecsService.getAllProductSpecifications();
        return ResponseEntity.ok(specs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductSpecs> updateProductSpecification(@PathVariable String id, @RequestBody ProductSpecs specDetails) {
        ProductSpecs updatedSpec = productSpecsService.updateProductSpecification(id, specDetails);
        return ResponseEntity.ok(updatedSpec);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductSpecification(@PathVariable String id) {
        productSpecsService.deleteProductSpecification(id);
        return ResponseEntity.noContent().build();
    }
}

