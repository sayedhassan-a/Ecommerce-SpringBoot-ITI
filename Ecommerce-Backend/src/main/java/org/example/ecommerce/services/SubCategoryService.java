package org.example.ecommerce.services;

import org.example.ecommerce.dtos.SubCategoryWithSpecificationDTO;
import org.example.ecommerce.mappers.SubCategoryMapper;
import org.example.ecommerce.models.SubCategory;
import org.example.ecommerce.models.SubCategorySpecification;
import org.example.ecommerce.repositories.SubCategoryRepository;
import org.example.ecommerce.repositories.SubCategorySpecificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubCategoryService {
    private final SubCategoryRepository subCategoryRepository;
    private final SubCategorySpecificationRepository subCategorySpecificationRepository;
    private final SubCategoryMapper subCategoryMapper;

    @Autowired
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
        SubCategorySpecification savedSpecification = null;

        // Set the structureId in SubCategory to link it with the specification (MongoDB ID)
        savedSubCategory.setStructureId(savedSpecification.getId());
        subCategoryRepository.save(savedSubCategory);  // Save again to update with structureId

        // Map back to combined DTO for the response
        return subCategoryMapper.toSubCategoryWithSpecificationDTO(savedSubCategory, savedSpecification);
    }



}
