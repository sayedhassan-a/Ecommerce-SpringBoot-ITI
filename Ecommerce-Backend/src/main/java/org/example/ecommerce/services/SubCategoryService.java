package org.example.ecommerce.services;

import org.example.ecommerce.dtos.SubCategoryDTO;
import org.example.ecommerce.dtos.SubCategoryWithSpecificationDTO;
import org.example.ecommerce.mappers.SubCategoryMapper;
import org.example.ecommerce.models.SubCategory;
import org.example.ecommerce.models.SubCategorySpecification;
import org.example.ecommerce.repositories.SubCategoryRepository;
import org.example.ecommerce.repositories.SubCategorySpecificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubCategoryService {
    private final SubCategoryRepository subCategoryRepository;
    private final SubCategorySpecificationRepository subCategorySpecificationRepository;
    private final SubCategoryMapper subCategoryMapper;


    public SubCategoryService(SubCategoryRepository subCategoryRepository, SubCategorySpecificationRepository subCategorySpecificationRepository, SubCategoryMapper subCategoryMapper) {
        this.subCategoryRepository = subCategoryRepository;
        this.subCategorySpecificationRepository = subCategorySpecificationRepository;
        this.subCategoryMapper = subCategoryMapper;
    }

    public SubCategoryWithSpecificationDTO createSubCategoryWithSpecification(SubCategoryWithSpecificationDTO dto) {
        // Map DTO to SubCategory entity
        SubCategory subCategory = subCategoryMapper.toSubCategory(dto.getSubCategory());
        // Save SubCategory (MySQL)
        SubCategory savedSubCategory = subCategoryRepository.save(subCategory);
        // Map DTO to SubCategorySpecification entity
        SubCategorySpecification subCategorySpecification = subCategoryMapper.toSubCategorySpecification(dto.getSubCategorySpecification());
        // Save SubCategorySpecification (MongoDB)
        SubCategorySpecification savedSpecification = subCategorySpecificationRepository.save(subCategorySpecification);
        // Set the structureId in SubCategory to link it with the specification (MongoDB ID)
        savedSubCategory.setStructureId(savedSpecification.getId());
        subCategoryRepository.save(savedSubCategory);  // Save again to update with structureId
        // Map back to combined DTO for the response
        return subCategoryMapper.toSubCategoryWithSpecificationDTO(savedSubCategory, savedSpecification);
    }
    public SubCategoryDTO getSubCategoryById(Long id) {
        return subCategoryRepository.findById(id)
                .map(subCategory -> subCategoryMapper.toSubCategoryDTO(subCategory)) // Assuming no specifications initially
                .orElse(null);
    }

    // Method to get subcategory with specifications by ID
    public SubCategoryWithSpecificationDTO getSubCategoryWithSpecifications(Long id) {
        // Fetch the subcategory
        SubCategory subCategory = subCategoryRepository.findById(id)
                .orElse(null); // or throw an exception if not found

        // Fetch the associated SubCategorySpecification using the structureId
        SubCategorySpecification subCategorySpecification = subCategorySpecificationRepository.findById(subCategory.getStructureId())
                .orElse(null); // or throw an exception if not found

        // Map to DTO
        return subCategoryMapper.toSubCategoryWithSpecificationDTO(subCategory, subCategorySpecification);
    }



}
