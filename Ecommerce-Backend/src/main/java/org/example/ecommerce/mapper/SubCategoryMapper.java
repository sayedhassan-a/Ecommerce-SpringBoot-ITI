package org.example.ecommerce.mapper;

import org.example.ecommerce.dto.SpecsDTO;
import org.example.ecommerce.dto.SubCategoryDTO;
import org.example.ecommerce.dto.SubCategorySpecificationDTO;
import org.example.ecommerce.dto.SubCategoryWithSpecificationDTO;
import org.example.ecommerce.models.Specs;
import org.example.ecommerce.models.SubCategory;
import org.example.ecommerce.models.SubCategorySpecification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubCategoryMapper {
    // **Mappings from DTO to Entities**

    // Map from SubCategoryDTO to SubCategory entity
    @Mapping(source = "structureId", target = "structureId")  // Map structureId to StructureId in SubCategory
    SubCategory toSubCategory(SubCategoryDTO subCategoryDTO);

    // Map from SubCategorySpecificationDTO to SubCategorySpecification entity
    SubCategorySpecification toSubCategorySpecification(SubCategorySpecificationDTO subCategorySpecificationDTO);

    // Map from SpecsDTO to Specs entity
    List<Specs> toSpecsList(List<SpecsDTO> specsDTOList);

    Specs toSpecs(SpecsDTO specsDTO);

    //**Mappings from Entities to DTO**

    // Map from SubCategory entity and SubCategorySpecification entity back to SubCategoryDTO
    @Mapping(source = "category.name", target = "categoryName")  // Assuming you have a Category object with a name
    @Mapping(source = "StructureId", target = "structureId")     // Map StructureId back to structureId in DTO
    SubCategoryDTO toSubCategoryDTO(SubCategory subCategory, SubCategorySpecification subCategorySpecification);

    // Map from SubCategorySpecification entity to SubCategorySpecificationDTO
    SubCategorySpecificationDTO toSubCategorySpecificationDTO(SubCategorySpecification subCategorySpecification);

    // Map from Specs entity to SpecsDTO
    List<SpecsDTO> toSpecsDTOList(List<Specs> specsList);

    SpecsDTO toSpecsDTO(Specs specs);

    // **Mapping for combined DTO**

    // Map from SubCategoryWithSpecificationDTO to entities (separately mapping subCategory and subCategorySpecification)
    default SubCategoryWithSpecificationDTO toSubCategoryWithSpecificationDTO(SubCategory subCategory, SubCategorySpecification subCategorySpecification) {
        SubCategoryWithSpecificationDTO dto = new SubCategoryWithSpecificationDTO();
        dto.setSubCategory(toSubCategoryDTO(subCategory, subCategorySpecification));
        dto.setSubCategorySpecification(toSubCategorySpecificationDTO(subCategorySpecification));
        return dto;
    }
}
