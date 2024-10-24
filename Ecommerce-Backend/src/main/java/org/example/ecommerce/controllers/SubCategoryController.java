package org.example.ecommerce.controllers;

import org.example.ecommerce.dtos.SubCategoryDTO;
import org.example.ecommerce.dtos.SubCategoryWithSpecificationDTO;
import org.example.ecommerce.services.SubCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/subcategories")
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    public SubCategoryController(SubCategoryService subCategoryService) {
        this.subCategoryService = subCategoryService;
    }

    // Endpoint to get a subcategory by ID
    @GetMapping("/{id}")
    public ResponseEntity<SubCategoryDTO> getSubCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(subCategoryService.getSubCategoryById(id)); // or return 404 if not found
    }

    // Endpoint to get a subcategory with its specifications by ID
    @GetMapping("/{id}/specifications")
    public ResponseEntity<SubCategoryWithSpecificationDTO> getSubCategoryWithSpecifications(@PathVariable Long id) {
        return ResponseEntity.ok(subCategoryService.getSubCategoryWithSpecifications(id));
    }

}
