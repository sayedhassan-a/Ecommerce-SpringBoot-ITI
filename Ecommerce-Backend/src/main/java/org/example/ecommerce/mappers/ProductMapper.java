package org.example.ecommerce.mapper;

import org.example.ecommerce.dto.ProductDTO;
import org.example.ecommerce.models.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper extends GenericMapper<Product,ProductDTO>{
}
