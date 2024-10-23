package org.example.ecommerce.controllers;

import jakarta.validation.Valid;
import org.example.ecommerce.dtos.CategoryDTO;
import org.example.ecommerce.dtos.SubCategoryDTO;
import org.example.ecommerce.dtos.SubCategoryWithSpecificationDTO;
import org.example.ecommerce.services.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class SubCategoryController {


    private final SubCategoryService subCategoryService;

    public SubCategoryController(SubCategoryService subCategoryService) {
        this.subCategoryService = subCategoryService;
    }
    @GetMapping("/subcategories")
    public List<SubCategoryDTO> getAllSubcategory(){
        return subCategoryService.getAllSubCategories();
    }

    // Endpoint to get a subcategory by ID
    @GetMapping("/subcategories/{id}")
    public ResponseEntity<SubCategoryDTO> getSubCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(subCategoryService.getSubCategoryById(id)); // or return 404 if not found
    }

    // Endpoint to get a subcategory with its specifications by ID
    @GetMapping("/subcategories/{id}/specifications")
    public ResponseEntity<SubCategoryWithSpecificationDTO> getSubCategoryWithSpecifications(@PathVariable Long id) {
        return ResponseEntity.ok(subCategoryService.getSubCategoryWithSpecifications(id));
    }
    @PostMapping("/subcategories")
    public ResponseEntity<SubCategoryWithSpecificationDTO> createCategory(@Valid @RequestBody SubCategoryWithSpecificationDTO subCategoryDTO){
        SubCategoryWithSpecificationDTO addedSubCategory = subCategoryService.createSubCategoryWithSpecification(subCategoryDTO);
        return new ResponseEntity<>(addedSubCategory, HttpStatus.CREATED);
    }

}
