package com.example.ecommerce.service;

import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.entity.Image;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.repository.ImageRepository;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;

    public ProductServiceImpl(ProductRepository productRepository, ImageRepository imageRepository) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
    }

    // Create a Product
    public Product save(Product product) {
        return productRepository.save(product);
    }


    // Update a Product
    public Product update(Product product) {
        return productRepository.save(product);
    }

    // Get a Product by ID
    public Optional<ProductDTO> getProductById(Integer id) {
        return Optional.ofNullable(productRepository.findById(id).map(ProductDTO::new).orElse(null));
    }

    // Get all Products
    public Page<Product> getAllProducts(Map<String, Object> filter, int page,
                                        int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products =
                productRepository.findAll(pageable);
        return products;
    }

    // Delete Product by ID
    public boolean deleteProduct(Integer id) {
        productRepository.deleteById(id);
        return true;
    }

    public Product addWithImages(Product product, List<String> images) {
        product.setImage(images.get(0));
        Product saved = productRepository.save(product);
        images.stream().skip(1).forEach((url)->{
            Image image = new Image();
            image.setProduct(product);
            image.setUrl(url);
            imageRepository.save(image);
        });
        return saved;
    }

    public Product updateWithImages(Product product,
                                    List<String> images) {
        if(!images.isEmpty()){
            product.setImage(images.get(0));
        }
        Product saved = productRepository.save(product);
        imageRepository.deleteAll(product.getImages());

        images.stream().skip(1).forEach((url)->{
            Image image = new Image();
            image.setProduct(product);
            image.setUrl(url);
            imageRepository.save(image);
        });
        return saved;
    }
}
