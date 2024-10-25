package org.example.ecommerce.mappers;

import org.example.ecommerce.dtos.ProductSpecsDTO;
import org.example.ecommerce.specification.ProductSpecs;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductSpecsMapper {
    ProductSpecsDTO toProductSpecsDTO(ProductSpecs productSpecs);
    List<ProductSpecsDTO> toProductSpecsDTOList(List<ProductSpecs> productSpecs);
}
