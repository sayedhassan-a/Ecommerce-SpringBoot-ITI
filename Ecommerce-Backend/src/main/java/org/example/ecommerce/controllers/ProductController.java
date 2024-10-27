package org.example.ecommerce.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ecommerce.dtos.*;
import org.example.ecommerce.dtos.*;
import org.example.ecommerce.models.Image;
import org.example.ecommerce.models.Product;
import org.example.ecommerce.services.ProductService;
import org.example.ecommerce.services.ProductSpecsService;
import org.example.ecommerce.specifications.ProductSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ProductSpecsService productSpecsService;
    private final ObjectMapper objectMapper;



    @Autowired
    public ProductController(ProductService productService, ProductSpecsService productSpecsService, ObjectMapper objectMapper) {
        this.productService = productService;
        this.productSpecsService = productSpecsService;
        this.objectMapper = objectMapper;
    }


    @GetMapping
    public Page<ProductResponseDTO> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return productService.getAllProductsDto(page, size);
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody ProductWithSpecsDTO productWithSpecsDTO) {
        Product savedProduct = productService.addProduct(productWithSpecsDTO);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long productId,
            @RequestBody UpdateProductDTO updateProductDTO) {
        ProductResponseDTO updatedProduct = productService.updateProduct(
                productId,
                updateProductDTO.getProductDto(),
                updateProductDTO.getProductSpecsDTO()
        );
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        ProductResponseDTO productResponse = productService.findProductById(id);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping("/{id}/specs")
    public ResponseEntity<ProductSpecs> getProductSpecs(@PathVariable Long id) {
        Product product=productService.getProductById(id);
        return ResponseEntity.ok(productSpecsService.getProductSpecificationById(product.getSpecsId()));
    }
    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponseDTO>> searchProducts(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String subCategory,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ProductResponseDTO> productsPage = productService.searchProducts(
                name, minPrice, maxPrice, category, subCategory, page, size);

        return ResponseEntity.ok(productsPage);
    }

    @GetMapping("/subcategory/{subCategoryId}")
    public Page<ProductResponseDTO> getProductsBySubCategory(
            @PathVariable Long subCategoryId,
            Pageable pageable) {
        return productService.getProductsBySubCategory(subCategoryId, pageable);
    }

    @GetMapping("/subcategory/{subCategoryId}/filter")
    public Page<ProductResponseDTO> getProducts(
            @PathVariable Long subCategoryId,    // <-- Ensure this is a @PathVariable
            @RequestParam String filters,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Map<String, List<String>> filtersMap = parseFilters(filters);
        return productService.getProductsByFilters(subCategoryId, filtersMap, page, size);
    }

    @GetMapping("/subcategory/{subCategoryId}/search")
    public Page<ProductResponseDTO> searchByName(
            @PathVariable Long subCategoryId,    // <-- Ensure this is a @PathVariable
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return productService.getProductsByName(subCategoryId, name, page,
                size);
    }

    private Map<String, List<String>> parseFilters(String filters) {
        try {
            return objectMapper.readValue(filters, new TypeReference<Map<String, List<String>>>() {});
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid filters format", e);
        }
    }


    @GetMapping("/{id}/stock")
    public ResponseEntity<ProductCartDTO> searchProducts(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findProductQuantityById(id));
    }



}
