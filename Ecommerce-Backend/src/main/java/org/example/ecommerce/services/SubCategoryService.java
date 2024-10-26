package org.example.ecommerce.services;

import org.example.ecommerce.dtos.CategoryDTO;
import org.example.ecommerce.dtos.SubCategoryDTO;
import org.example.ecommerce.dtos.SubCategoryWithSpecificationDTO;
import org.example.ecommerce.mappers.SubCategoryMapper;
import org.example.ecommerce.models.Category;
import org.example.ecommerce.models.SubCategory;
import org.example.ecommerce.models.SubCategorySpecification;
import org.example.ecommerce.repositories.CategoryRepository;
import org.example.ecommerce.repositories.SubCategoryRepository;
import org.example.ecommerce.repositories.SubCategorySpecificationRepository;
import org.example.ecommerce.system.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubCategoryService {
    private final SubCategoryRepository subCategoryRepository;
    private final SubCategorySpecificationRepository subCategorySpecificationRepository;
    private final SubCategoryMapper subCategoryMapper;
    private final CategoryRepository categoryRepository;


    public SubCategoryService(SubCategoryRepository subCategoryRepository, SubCategorySpecificationRepository subCategorySpecificationRepository, SubCategoryMapper subCategoryMapper, CategoryRepository categoryRepository) {
        this.subCategoryRepository = subCategoryRepository;
        this.subCategorySpecificationRepository = subCategorySpecificationRepository;
        this.subCategoryMapper = subCategoryMapper;
        this.categoryRepository = categoryRepository;
    }

    public List<SubCategoryDTO> getAllSubCategories(){
        return subCategoryRepository.findAll()
                .stream()
                .map(subCategoryMapper::toSubCategoryDTO)
                .collect(Collectors.toList());
    }
    public SubCategoryWithSpecificationDTO createSubCategoryWithSpecification(SubCategoryWithSpecificationDTO dto) {
        Category category = categoryRepository.findByName(dto.getSubCategory().getCategoryName());
        // Map DTO to SubCategory entity
        SubCategory subCategory = subCategoryMapper.toSubCategory(dto.getSubCategory());
        subCategory.setCategory(category);
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
                .orElse(null);
        // Map to DTO
        return subCategoryMapper.toSubCategoryWithSpecificationDTO(subCategory, subCategorySpecification);
    }

    public SubCategoryWithSpecificationDTO updateSubCategoryWithSpecification(Long id, SubCategoryWithSpecificationDTO dto) {
        // Fetch the existing SubCategory from MySQL
        SubCategory existingSubCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory not found with id " + id));

        // Fetch the associated SubCategorySpecification from MongoDB using the structureId
        SubCategorySpecification existingSpecification = subCategorySpecificationRepository.findById(existingSubCategory.getStructureId())
                .orElseThrow(() -> new ResourceNotFoundException("SubCategorySpecification not found with id " + existingSubCategory.getStructureId()));

        // Update the SubCategory fields from the DTO
        existingSubCategory.setName(dto.getSubCategory().getName());

        // Update category if provided
        if (dto.getSubCategory().getCategoryName() != null) {
            Category category = categoryRepository.findByName(dto.getSubCategory().getCategoryName());
            existingSubCategory.setCategory(category);
        }

        // Update the SubCategorySpecification fields from the DTO
        existingSpecification.setName(dto.getSubCategorySpecification().getName());
        existingSpecification.setSpecs(subCategoryMapper.toSpecsList(dto.getSubCategorySpecification().getSpecs()));

        // Save the updated SubCategory (MySQL)
        SubCategory updatedSubCategory = subCategoryRepository.save(existingSubCategory);

        // Save the updated SubCategorySpecification (MongoDB)
        SubCategorySpecification updatedSpecification = subCategorySpecificationRepository.save(existingSpecification);

        // Map back to combined DTO for the response
        return subCategoryMapper.toSubCategoryWithSpecificationDTO(updatedSubCategory, updatedSpecification);
    }


    public void deleteSubCategoryWithSpecification(Long id) {
        // Fetch the existing SubCategory from MySQL
        SubCategory existingSubCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory not found with id " + id));

        // Fetch the associated SubCategorySpecification from MongoDB using the structureId
        SubCategorySpecification existingSpecification = subCategorySpecificationRepository.findById(existingSubCategory.getStructureId())
                .orElseThrow(() -> new ResourceNotFoundException("SubCategorySpecification not found with id " + existingSubCategory.getStructureId()));

        // Delete the SubCategory from MySQL
        subCategoryRepository.delete(existingSubCategory);

        // Delete the SubCategorySpecification from MongoDB
        subCategorySpecificationRepository.delete(existingSpecification);
    }



}
