package org.example.ecommerce.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ecommerce.dtos.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
//    @PutMapping("/{productId}")
//    public ResponseEntity<ProductResponseDTO> updateProduct(
//            @PathVariable Long productId,
//            @RequestBody UpdateProductDTO updateProductDTO) {
//        ProductResponseDTO updatedProduct = productService.updateProduct(
//                productId,
//                updateProductDTO.getProductDto(),
//                updateProductDTO.getProductSpecsDTO()
//        );
//        return ResponseEntity.ok(updatedProduct);
//    }

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
    public Page<SimpleProductDTO> getProductsBySubCategory(
            @PathVariable Long subCategoryId,
            Pageable pageable) {
        return productService.getProductsBySubCategory(subCategoryId, pageable);
    }

    @GetMapping("/subcategory/{subCategoryId}/filter")
    public Page<SimpleProductDTO> getProducts(
            @PathVariable Long subCategoryId,
            @RequestParam String filters,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Map<String, List<String>> filtersMap = parseFilters(filters);

        // Check if filtersMap is null, meaning no filters were applied
        if (filtersMap == null) {
            return productService.getAllProductsBySubCategory(subCategoryId, page, size);
        }

        return productService.getProductsByFilters(subCategoryId, filtersMap, page, size);
    }


    @GetMapping("/subcategory/{subCategoryId}/search")
    public Page<SimpleProductDTO> searchByName(
            @PathVariable Long subCategoryId,    // <-- Ensure this is a @PathVariable
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return productService.getProductsByName(subCategoryId, name, page,
                size);
    }

    private Map<String, List<String>> parseFilters(String filters) {
        try {
            Map<String, List<String>> filterMap = objectMapper.readValue(filters, new TypeReference<Map<String, List<String>>>() {});
            // Check if the map is empty
            return filterMap.isEmpty() ? null : filterMap;
        } catch (IOException e) {
            return null;
        }
    }



    @GetMapping("/{id}/stock")
    public ResponseEntity<ProductCartDTO> searchProducts(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findProductQuantityById(id));
    }



    @GetMapping("/latest")
    public ResponseEntity<List<SimpleProductDTO>> getLatestProducts() {
        List<SimpleProductDTO> latestProducts = productService.getLatestProducts();
        return ResponseEntity.ok(latestProducts);
    }
    @GetMapping("/onsale")
    public ResponseEntity<Page<SimpleProductDTO>> getProductsOnSale(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SimpleProductDTO> products = productService.getProductsOnSale(page, size);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/flash-sale")
    public ResponseEntity<Page<SimpleProductDTO>> getFlashSaleProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<SimpleProductDTO> products = productService.getFlashSaleProducts(page,size);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

}
