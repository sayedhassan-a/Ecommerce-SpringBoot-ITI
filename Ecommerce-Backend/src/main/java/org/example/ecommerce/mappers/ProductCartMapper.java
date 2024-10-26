package org.example.ecommerce.mappers;

import org.example.ecommerce.dtos.ProductCartDTO;
import org.example.ecommerce.dtos.ProductResponseDTO;
import org.example.ecommerce.models.Product;
import org.example.ecommerce.specification.ProductSpecs;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductCartMapper extends GenericMapper<Product,
        ProductCartDTO>{
}
