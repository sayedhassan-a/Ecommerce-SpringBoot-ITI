package org.example.ecommerce.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ecommerce.dtos.*;
import org.example.ecommerce.mappers.ProductCartMapper;
import org.example.ecommerce.mappers.ProductMapper;
import org.example.ecommerce.models.Image;
import org.example.ecommerce.models.Product;
import org.example.ecommerce.repositories.ProductRepository;
import org.example.ecommerce.repositories.ProductSpecificationRepository;
import org.example.ecommerce.specifications.ProductSpecs;
import org.example.ecommerce.system.exceptions.ProductNotFoundException;
import org.example.ecommerce.system.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductSpecificationRepository productSpecsRepository;
    private final ProductMapper productMapper;
    private final ProductCartMapper productCartMapper;
    private final ProductSpecsService productSpecsService;

    @Autowired
    public ProductService(ProductRepository productRepository,
                          ProductSpecificationRepository productSpecificationRepository, ProductMapper productMapper, ProductCartMapper productCartMapper, ProductSpecsService productSpecsService) {
        this.productRepository = productRepository;
        this.productSpecsRepository = productSpecificationRepository;
        this.productMapper = productMapper;
        this.productCartMapper = productCartMapper;
        this.productSpecsService = productSpecsService;

    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product addProduct(ProductWithSpecsDTO productWithSpecsDTO) {
        ProductRequestDTO productDTO = productWithSpecsDTO.getProductDto();
        ProductSpecsDTO specsDTO = productWithSpecsDTO.getProductSpecsDTO();

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setStock(productDTO.getStock());
        product.setImage(productDTO.getImages().get(0));
        product.setSalePercentage(productDTO.getSalePercentage());
        product.setImages(productDTO.getImages().stream().skip(1).map(image -> {
            Image image1 = new Image();
            image1.setUrl(image);
            image1.setProduct(product);
            return image1;
        }).collect(Collectors.toSet()));
        product.setBrandName(productDTO.getBrandName());
        product.setSubCategory(productDTO.getSubCategory());

        // Save product in MySQL
        Product savedProduct = productRepository.save(product);

        // Create ProductSpecs for MongoDB
        ProductSpecs specs = new ProductSpecs();
        specs.setProductId(savedProduct.getId().toString());
        specs.setKey(specsDTO.getKey());
        specs.setValue(specsDTO.getValue());

        // Save specs in MongoDB
        ProductSpecs savedSpecs = productSpecsService.saveProductSpecification(specs);

        // Update product with specsId (MongoDB ID) and save again in MySQL
        savedProduct.setSpecsId(savedSpecs.getId());
        return productRepository.save(savedProduct);
    }


    @Transactional
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

    @Transactional
    public void deleteProduct(Long productId) {
        productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId)).setDeleted(true);

    }


    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

    }

    public Page<ProductResponseDTO> getAllProductsDto(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Product> productPage = productRepository.findAllByDeleted(false,pageable);

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

        List<ProductSpecs> matchingSpecs = productSpecsRepository.findByDynamicFilters(filters);
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

    public Page<ProductResponseDTO> getProductsByName(Long subCategoryId,
                                                      String name, int page,
                                                      int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> matchingProducts =
                productRepository.findBySubCategoryIdAndNameLikeIgnoreCaseAndDeletedFalse(subCategoryId,
                "%"+name+"%", pageable);

        return matchingProducts.map(product -> {
            return productMapper.toProductResponseDTO(product,
                    productSpecsRepository.findById(product.getSpecsId()).orElse(null));
        });

    }

    public ProductCartDTO findProductQuantityById(Long id) {
        return productCartMapper.toDTO(
                productRepository.findById(id).orElseThrow(
                        ()->new ProductNotFoundException(id)));
    }




    public ProductResponseDTO updateProduct(Long productId, ProductRequestDTO productRequestDTO, ProductSpecsDTO productSpecsDTO) {

        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()) {
            throw new RuntimeException("Product not found with ID: " + productId);
        }
        Product product = optionalProduct.get();

        product.setName(productRequestDTO.getName());
        product.setPrice(productRequestDTO.getPrice());
        product.setDescription(productRequestDTO.getDescription());
        product.setStock(productRequestDTO.getStock());
        product.setBrandName(productRequestDTO.getBrandName());
        product.setSubCategory(productRequestDTO.getSubCategory());
        product.setSalePercentage(product.getSalePercentage());
        product.setImage(productRequestDTO.getImages().isEmpty() ? null : productRequestDTO.getImages().get(0)); // Set main image if present

        Product updatedProduct = productRepository.save(product);

        Optional<ProductSpecs> optionalSpecs = productSpecsRepository.findById(updatedProduct.getSpecsId());
        ProductSpecs productSpecs;
        if (optionalSpecs.isPresent()) {
            productSpecs = optionalSpecs.get();
        } else {
            productSpecs = new ProductSpecs();
            productSpecs.setId(updatedProduct.getSpecsId());
        }
        productSpecs.setProductId(updatedProduct.getId().toString());
        productSpecs.setKey(productSpecsDTO.getKey());
        productSpecs.setValue(productSpecsDTO.getValue());

        //MongoDB
        ProductSpecs updatedSpecs = productSpecsRepository.save(productSpecs);

        return productMapper.toProductResponseDTO(updatedProduct, updatedSpecs);
    }



   // latest products
   public List<ProductResponseDTO> getLatestProducts() {
       List<Product> latestProducts = productRepository.findTop10ByOrderByCreatedAtDesc();
       return latestProducts.stream()
               .map(product -> productMapper.toProductResponseDTO(
                       product, productSpecsRepository.findById(product.getSpecsId()).orElse(null)))
               .collect(Collectors.toList());
   }


    // fetching products on sale
    public Page<ProductResponseDTO> getProductsOnSale(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> saleProducts = productRepository.findProductsOnSale(pageable);

        return saleProducts.map(product -> productMapper.toProductResponseDTO(
                product, productSpecsRepository.findById(product.getSpecsId()).orElse(null)));
    }


    // fetching products on flash sale
    public Page<ProductResponseDTO> getFlashSaleProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> flashSaleProducts = productRepository.findFlashSaleProducts(pageable);

        return flashSaleProducts.map(product -> productMapper.toProductResponseDTO(
                product, productSpecsRepository.findById(product.getSpecsId()).orElse(null)));
    }

}
