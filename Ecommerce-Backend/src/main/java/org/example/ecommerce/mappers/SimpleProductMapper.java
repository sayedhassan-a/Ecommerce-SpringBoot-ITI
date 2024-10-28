package org.example.ecommerce.mappers;

import org.example.ecommerce.dtos.SimpleProductDTO;
import org.example.ecommerce.models.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SimpleProductMapper extends GenericMapper<Product,
        SimpleProductDTO> {
}
