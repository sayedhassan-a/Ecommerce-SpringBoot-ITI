package org.example.ecommerce.services;

import org.example.ecommerce.dtos.*;
import org.example.ecommerce.mappers.ProductCartMapper;
import org.example.ecommerce.mappers.ProductMapper;
import org.example.ecommerce.mappers.SimpleProductMapper;
import org.example.ecommerce.models.Image;
import org.example.ecommerce.models.Product;
import org.example.ecommerce.models.SubCategory;
import org.example.ecommerce.repositories.ProductRepository;
import org.example.ecommerce.repositories.ProductSpecificationRepository;
import org.example.ecommerce.repositories.SubCategoryRepository;
import org.example.ecommerce.specifications.ProductSpecs;
import org.example.ecommerce.system.exceptions.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;


@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductSpecificationRepository productSpecsRepository;
    private final ProductMapper productMapper;
    private final ProductCartMapper productCartMapper;
    private final ProductSpecsService productSpecsService;
    private final SimpleProductMapper simpleProductMapper;
    private final SubCategoryRepository subCategoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository,
                          ProductSpecificationRepository productSpecificationRepository, ProductMapper productMapper, ProductCartMapper productCartMapper, ProductSpecsService productSpecsService, SimpleProductMapper simpleProductMapper, SubCategoryRepository subCategoryRepository) {
        this.productRepository = productRepository;
        this.productSpecsRepository = productSpecificationRepository;
        this.productMapper = productMapper;
        this.productCartMapper = productCartMapper;
        this.productSpecsService = productSpecsService;
        this.simpleProductMapper = simpleProductMapper;
        this.subCategoryRepository = subCategoryRepository;
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

        // MySQL
        Product savedProduct = productRepository.save(product);

        //MongoDB
        ProductSpecs specs = new ProductSpecs();
        specs.setProductId(savedProduct.getId().toString());
        specs.setKey(specsDTO.getKey());
        specs.setValue(specsDTO.getValue());

        // MongoDB
        ProductSpecs savedSpecs = productSpecsService.saveProductSpecification(specs);

        // (MongoDB ID) and save again in MySQL
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

    public Page<SimpleProductDTO> getProductsBySubCategory(Long subCategoryId,
                                            Pageable pageable) {
        Page<Product> products = productRepository.findBySubCategory(subCategoryId, pageable);
        return products.map(simpleProductMapper::toDTO);
    }

    private ProductSpecs fetchProductSpecs(String specsId) {
        return new ProductSpecs(); // Replace with actual fetching logic
    }

    public Page<SimpleProductDTO> getProductsByFilters(Long subCategoryId, Map<String, List<String>> filters, int page, int size) {

        List<ProductSpecs> matchingSpecs = productSpecsRepository.findByDynamicFilters(filters);
        List<String> specsIds = matchingSpecs.stream().map(ProductSpecs::getId).collect(Collectors.toList());

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> matchingProducts = productRepository.findBySubCategoryIdAndSpecsIds(subCategoryId, specsIds, pageable);

        return matchingProducts.map(simpleProductMapper::toDTO);
    }

    public Page<SimpleProductDTO> getProductsByName(Long subCategoryId,
                                                      String name, int page,
                                                      int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> matchingProducts =
                productRepository.findBySubCategoryIdAndNameLikeIgnoreCaseAndDeletedFalse(subCategoryId,
                "%"+name+"%", pageable);

        return matchingProducts.map(simpleProductMapper::toDTO);

    }

    public ProductCartDTO findProductQuantityById(Long id) {
        return productCartMapper.toDTO(
                productRepository.findById(id).orElseThrow(
                        ()->new ProductNotFoundException(id)));
    }


    public ProductResponseDTO updateProduct(Long productId, ProductResponseDTO productRequestDTO) {

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
        SubCategory subCategory = subCategoryRepository.findBy(productRequestDTO.getSubCategoryId());
        product.setSubCategory(subCategory);
        product.setSalePercentage(productRequestDTO.getSalePercentage());
        product.setImage(productRequestDTO.getImage()); // Set main image if present

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
        productSpecs.setKey(productRequestDTO.getProductSpecs().getKey());
        productSpecs.setValue(productRequestDTO.getProductSpecs().getValue());

        //MongoDB
        ProductSpecs updatedSpecs = productSpecsRepository.save(productSpecs);

        return productMapper.toProductResponseDTO(updatedProduct, updatedSpecs);
    }

    // latest products
   public List<SimpleProductDTO> getLatestProducts() {
       List<Product> latestProducts = productRepository.findTop10ByOrderByCreatedAtDesc();
       return latestProducts.stream()
               .map(simpleProductMapper::toDTO)
               .collect(Collectors.toList());
   }


    // fetching products on sale
    public Page<SimpleProductDTO> getProductsOnSale(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> saleProducts = productRepository.findProductsOnSale(pageable);

        return saleProducts.map(simpleProductMapper::toDTO);
    }


    // fetching products on flash sale
    public Page<SimpleProductDTO> getFlashSaleProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> flashSaleProducts = productRepository.findFlashSaleProducts(pageable);

        return flashSaleProducts.map(simpleProductMapper::toDTO);
    }



    /*public Result addProductWithSpecs(ProductWithSpecsDTO productWithSpecsDTO) {
        ProductRequestDTO productDTO = productWithSpecsDTO.getProductDto();
        ProductSpecsDTO specsDTO = productWithSpecsDTO.getProductSpecsDTO();

        // Initialize and populate the Product entity
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setStock(productDTO.getStock());
        product.setImage(productDTO.getImages().get(0));
        product.setImages(productDTO.getImages().stream().skip(1).map(image -> {
            Image image1 = new Image();
            image1.setUrl(image);
            image1.setProduct(product);
            return image1;
        }).collect(Collectors.toSet()));
        product.setBrandName(productDTO.getBrandName());
        product.setSubCategory(productDTO.getSubCategory());

        // Save the Product entity
        Product savedProduct = createProduct(product);

        // Initialize and populate the ProductSpecs entity
        ProductSpecs specs = new ProductSpecs();
        specs.setProductId(savedProduct.getId().toString());
        specs.setKey(specsDTO.getKey());
        specs.setValue(specsDTO.getValue());

        // Save the ProductSpecs and update the product with specsId
        ProductSpecs savedSpecs = productSpecsService.saveProductSpecification(specs);
        savedProduct.setSpecsId(savedSpecs.getId());
        createProduct(savedProduct);  // Save updated product with specsId

        return new Result(true, StatusCode.SUCCESS, "Product added successfully", savedProduct);
    }*/


    public Page<SimpleProductDTO> getAllProductsBySubCategory(Long subCategoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> product=productRepository.findBySubCategoryId(subCategoryId,pageable);
        return product.map(simpleProductMapper::toDTO);
    }






}
