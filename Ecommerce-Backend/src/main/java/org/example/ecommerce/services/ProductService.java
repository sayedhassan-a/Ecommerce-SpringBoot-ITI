package org.example.ecommerce.services;

import org.example.ecommerce.dtos.ProductResponseDTO;
import org.example.ecommerce.mappers.ProductMapper;
import org.example.ecommerce.models.Product;
import org.example.ecommerce.repositories.ProductRepository;
import org.example.ecommerce.specification.ProductSpecificationRepository;
import org.example.ecommerce.specification.ProductSpecs;
import org.example.ecommerce.system.exceptions.ProductNotFoundException;
import org.example.ecommerce.system.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductSpecificationRepository productSpecsRepository;
    private final ProductMapper productMapper;

    @Autowired
    public ProductService(ProductRepository productRepository,
                              ProductSpecificationRepository productSpecificationRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productSpecsRepository = productSpecificationRepository;
        this.productMapper = productMapper;
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }


    public Product updateProduct(Long id, Product product) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(product.getName());
                    existingProduct.setPrice(product.getPrice());
                    existingProduct.setDescription(product.getDescription());
                    existingProduct.setStock(product.getStock());
                    existingProduct.setImage(product.getImage());
                    existingProduct.setBrandName(product.getBrandName());
                    existingProduct.setSubCategory(product.getSubCategory());
                    return productRepository.save(existingProduct);
                }).orElseThrow(() -> new ProductNotFoundException(id));
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }


    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    public Page<ProductResponseDTO> getAllProductsDto(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Product> productPage = productRepository.findAll(pageable);

            return productPage.map(product -> {
                ProductSpecs productSpecs = null;
                if (product.getSpecsId() != null) {
                    productSpecs = productSpecsRepository.findById(product.getSpecsId())
                            .orElse(null);
                }
                return productMapper.toProductResponseDTO(product, productSpecs);
            });
        } catch (Exception e) {
            System.err.println("Error while fetching products: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Internal server error. Please try again later.");
        }
    }

    public Page<ProductResponseDTO> searchProducts(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        String processedKeyword = keyword.toLowerCase().replaceAll("\\s+", " "); // Normalize spaces and case

        // Perform the search and map each Product to ProductResponseDTO with ProductSpecs included
        return productRepository.searchProductsByName(processedKeyword, pageable)
                .map(product -> {
                    // Fetch ProductSpecs from MongoDB by specsId
                    ProductSpecs productSpecs = productSpecsRepository.findById(product.getSpecsId())
                            .orElse(null); // Use null if specs are optional

                    // Map to ProductResponseDTO
                    return productMapper.toProductResponseDTO(product, productSpecs);
                });
    }

    public ProductResponseDTO findProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        ProductSpecs productSpecs = productSpecsRepository.findById(product.getSpecsId())
                .orElse(null);
        return productMapper.toProductResponseDTO(product, productSpecs);
        }



    public Page<ProductResponseDTO> searchProducts(
            String name,
            Integer minPrice,
            Integer maxPrice,
            String category,
            String subCategory,
            int page,
            int size) {

        Integer effectiveMinPrice = (minPrice != null) ? minPrice : 0;
        Integer effectiveMaxPrice = (maxPrice != null) ? maxPrice : 60000;

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productsPage = productRepository.searchProducts(
                name, effectiveMinPrice, effectiveMaxPrice, category, subCategory, pageable
        );

        // Map each Product to ProductResponseDTO with ProductSpecs included
        return productsPage.map(product -> {
            // Fetch ProductSpecs from MongoDB by specsId
            ProductSpecs productSpecs = productSpecsRepository.findById(product.getSpecsId())
                    .orElse(null); // Use null if specs are optional

            // Map to ProductResponseDTO
            return productMapper.toProductResponseDTO(product, productSpecs);
        });
    }

}
