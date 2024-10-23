package org.example.ecommerce.mappers;

import org.example.ecommerce.dtos.SpecsDTO;
import org.example.ecommerce.dtos.SubCategoryDTO;
import org.example.ecommerce.dtos.SubCategorySpecificationDTO;
import org.example.ecommerce.dtos.SubCategoryWithSpecificationDTO;
import org.example.ecommerce.models.Specs;
import org.example.ecommerce.models.SubCategory;
import org.example.ecommerce.models.SubCategorySpecification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubCategoryMapper {
    // **Mappings from DTO to Entities**
  // Map structureId to StructureId in SubCategory
    SubCategory toSubCategory(SubCategoryDTO subCategoryDTO);

    SubCategorySpecification toSubCategorySpecification(SubCategorySpecificationDTO subCategorySpecificationDTO);

    List<Specs> toSpecsList(List<SpecsDTO> specsDTOList);

    Specs toSpecs(SpecsDTO specsDTO);

    // **Mappings from Entities to DTO**

    // Map from SubCategory entity and SubCategorySpecification entity back to SubCategoryDTO
    // Correct the mapping to match the category relationship
    @Mapping(source = "category.name", target = "categoryName")
    SubCategoryDTO toSubCategoryDTO(SubCategory subCategory);

    // Correct method signature to map lists of entities to lists of DTOs
    List<SubCategoryDTO> toSubCategoryDTOList(List<SubCategory> subCategories);

    SubCategorySpecificationDTO toSubCategorySpecificationDTO(SubCategorySpecification subCategorySpecification);

    List<SpecsDTO> toSpecsDTOList(List<Specs> specsList);

    SpecsDTO toSpecsDTO(Specs specs);

    // **Mapping for combined DTO**

    // Map from SubCategoryWithSpecificationDTO to entities (separately mapping subCategory and subCategorySpecification)
    default SubCategoryWithSpecificationDTO toSubCategoryWithSpecificationDTO(SubCategory subCategory, SubCategorySpecification subCategorySpecification) {
        SubCategoryWithSpecificationDTO dto = new SubCategoryWithSpecificationDTO();
        dto.setSubCategory(toSubCategoryDTO(subCategory));
        dto.setSubCategorySpecification(toSubCategorySpecificationDTO(subCategorySpecification));
        return dto;
    }
}
