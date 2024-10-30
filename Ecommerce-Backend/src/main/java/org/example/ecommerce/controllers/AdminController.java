package org.example.ecommerce.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.example.ecommerce.dtos.*;
import org.example.ecommerce.dtos.adminConverters.AdminDtoToAdminConverter;
import org.example.ecommerce.dtos.adminConverters.AdminToAdminDtoConverter;
import org.example.ecommerce.models.Admin;
import org.example.ecommerce.models.Image;
import org.example.ecommerce.models.Product;
import org.example.ecommerce.services.AdminService;
import org.example.ecommerce.services.ProductService;
import org.example.ecommerce.services.ProductSpecsService;
import org.example.ecommerce.specifications.ProductSpecs;
import org.example.ecommerce.system.Result;
import org.example.ecommerce.system.StatusCode;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admins")
@Validated
@AllArgsConstructor
public class AdminController {

    private AdminService adminService;
    private AdminDtoToAdminConverter adminDtoToAdminConverter;
    private AdminToAdminDtoConverter adminToAdminDtoConverter;
    private ProductService productService;
    private ProductSpecsService productSpecsService;

    // Create a new admin
    @PostMapping
    public Result createAdmin(@Valid @RequestBody Admin admin) {
        Admin savedAdmin = adminService.save(admin);
        // Convert saved entity back to DTO
        AdminDTO savedAdminDto = adminToAdminDtoConverter.convert(savedAdmin);
        return new Result(true, StatusCode.SUCCESS, "Admin created successfully", savedAdminDto);
    }

    // Retrieve all admins
    @GetMapping
    public Result findAllAdmins() {
        List<Admin> admins = adminService.findAll();
        // Convert list of entities to DTOs
        List<AdminDTO> adminDtos = admins.stream()
                .map(adminToAdminDtoConverter::convert)
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Admins retrieved successfully", adminDtos);
    }

    // Retrieve admin by ID
    @GetMapping("/{id}")
    public Result findAdminById(@PathVariable Long id) {
        Admin foundAdmin = adminService.findById(id);
        AdminDTO adminDto = adminToAdminDtoConverter.convert(foundAdmin);
        return new Result(true, StatusCode.SUCCESS, "Admin retrieved successfully", adminDto);
    }

    // Retrieve admin by email
    @GetMapping("/email")
    public Result findAdminByEmail(@RequestParam String email) {
        Admin foundAdmin = adminService.findByEmail(email);
        AdminDTO adminDto = adminToAdminDtoConverter.convert(foundAdmin);
        return new Result(true, StatusCode.SUCCESS, "Admin retrieved successfully", adminDto);
    }

    // Update an existing admin
    @PutMapping("/{id}")
    public Result updateAdmin(@PathVariable Long id, @Valid @RequestBody AdminDTO adminDto) {
        // Convert DTO to entity for update
        Admin admin = adminDtoToAdminConverter.convert(adminDto);
        Admin updatedAdmin = adminService.update(id, admin);
        // Convert updated entity back to DTO
        AdminDTO updatedAdminDto = adminToAdminDtoConverter.convert(updatedAdmin);
        return new Result(true, StatusCode.SUCCESS, "Admin updated successfully", updatedAdminDto);
    }

    // Delete an admin
    @DeleteMapping("/{id}")
    public Result deleteAdmin(@PathVariable Long id) {
        adminService.delete(id);
        return new Result(true, StatusCode.SUCCESS, "Admin deleted successfully", null);
    }


    // this part is for managing products

    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(@RequestBody ProductWithSpecsDTO productWithSpecsDTO) {
        Product savedProduct = productService.addProduct(productWithSpecsDTO);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @DeleteMapping("/products/{id}")
    public Result deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new Result(true, StatusCode.SUCCESS, "Product deleted successfully", null);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        ProductResponseDTO productResponse = productService.findProductById(id);
        return  ResponseEntity.ok(productResponse);
    }


    @GetMapping("/products")
    public Result getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ProductResponseDTO> products = productService.getAllProductsDto(page, size);
        return new Result(true, StatusCode.SUCCESS, "Products retrieved successfully", products);
    }


    @PutMapping("/products/{id}")
    public ProductResponseDTO updateProduct(
            @PathVariable Long id,
            @RequestBody ProductResponseDTO updateProductDTO) {
        ProductResponseDTO updatedProduct = productService.updateProduct(
                id,
                updateProductDTO
        );
        return updatedProduct;
    }



    @GetMapping("products/search")
    public Page<ProductResponseDTO> searchProducts(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "page", defaultValue = "0" , required = false) int page,
            @RequestParam(value = "size", defaultValue = "10",  required = false) int size) {

        return productService.searchProducts(keyword, page, size);
    }



     /* @PostMapping("/products")
    public Result addProduct(@RequestBody ProductWithSpecsDTO productWithSpecsDTO) {
        ProductRequestDTO productDTO = productWithSpecsDTO.getProductDto();
        ProductSpecsDTO specsDTO = productWithSpecsDTO.getProductSpecsDTO();

        // Use existing ProductService method
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setStock(productDTO.getStock());
        product.setImage(productDTO.getImages().get(0));
        product.setImages(productDTO.getImages().stream().skip(1).map(image->{
            Image image1=new Image();
            image1.setUrl(image);
            image1.setProduct(product);
            return image1;
        }).collect(Collectors.toSet()));
        product.setBrandName(productDTO.getBrandName());
        product.setSubCategory(productDTO.getSubCategory());

        Product savedProduct = productService.createProduct(product);
        ProductSpecs specs = new ProductSpecs();
        specs.setProductId(savedProduct.getId().toString());
        specs.setKey(specsDTO.getKey());
        specs.setValue(specsDTO.getValue());

        ProductSpecs savedSpecs = productSpecsService.saveProductSpecification(specs);
        savedProduct.setSpecsId(savedSpecs.getId());
        productService.createProduct(savedProduct);

        return new Result(true, StatusCode.SUCCESS, "Product added successfully", savedProduct);
    }*/

}