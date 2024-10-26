package org.example.ecommerce.services;

import org.example.ecommerce.dtos.ProductCartDTO;
import org.example.ecommerce.dtos.ProductResponseDTO;
import org.example.ecommerce.mappers.ProductCartMapper;
import org.example.ecommerce.mappers.ProductMapper;
import org.example.ecommerce.models.Product;
import org.example.ecommerce.repositories.ProductRepository;
import org.example.ecommerce.repositories.ProductSpecificationRepository;
import org.example.ecommerce.specifications.ProductSpecs;
import org.example.ecommerce.system.exceptions.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductSpecificationRepository productSpecsRepository;
    private final ProductMapper productMapper;
    private final ProductCartMapper productCartMapper;

    @Autowired
    public ProductService(ProductRepository productRepository,
                          ProductSpecificationRepository productSpecificationRepository, ProductMapper productMapper, ProductCartMapper productCartMapper) {
        this.productRepository = productRepository;
        this.productSpecsRepository = productSpecificationRepository;
        this.productMapper = productMapper;
        this.productCartMapper = productCartMapper;
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
        String processedKeyword = keyword.toLowerCase().replaceAll("\\s+", " ");

        return productRepository.searchProductsByName(processedKeyword, pageable)
                .map(product -> {
                    ProductSpecs productSpecs = productSpecsRepository.findById(product.getSpecsId())
                            .orElse(null); // Use null if specs are optional

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

    public Page<ProductResponseDTO> getProductsBySubCategory(Long subCategoryId, Pageable pageable) {
        Page<Product> products = productRepository.findBySubCategory(subCategoryId, pageable);
        return products.map(product -> {
            ProductSpecs productSpecs = fetchProductSpecs(product.getSpecsId());
            return productMapper.toProductResponseDTO(product, productSpecs);
        });
    }

    private ProductSpecs fetchProductSpecs(String specsId) {
        return new ProductSpecs(); // Replace with actual fetching logic
    }

    public Page<ProductResponseDTO> getProductsByFilters(Long subCategoryId, Map<String, List<String>> filters, int page, int size) {

        List<String> keys = new ArrayList<>(filters.keySet());
        List<String> values = filters.values().stream().flatMap(List::stream).collect(Collectors.toList());

        List<ProductSpecs> matchingSpecs = productSpecsRepository.findByDynamicFilters(keys, values);
        List<String> specsIds = matchingSpecs.stream().map(ProductSpecs::getId).collect(Collectors.toList());

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> matchingProducts = productRepository.findBySubCategoryIdAndSpecsIds(subCategoryId, specsIds, pageable);

        return matchingProducts.map(product -> {
            ProductSpecs productSpecs = matchingSpecs.stream()
                    .filter(spec -> spec.getId().equals(product.getSpecsId()))
                    .findFirst().orElse(null);
            return productMapper.toProductResponseDTO(product, productSpecs);
        });
    }

    public ProductCartDTO findProductQuantityById(Long id) {
        return productCartMapper.toDTO(
                productRepository.findById(id).orElseThrow(
                        ()->new ProductNotFoundException(id)));
    }
}
