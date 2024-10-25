package org.example.ecommerce.mappers;


import org.example.ecommerce.dtos.ProductResponseDTO;
import org.example.ecommerce.models.Product;
import org.example.ecommerce.specification.ProductSpecs;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "product.id", target = "id")
    @Mapping(source = "product.name", target = "name")
    @Mapping(source = "product.price", target = "price")
    @Mapping(source = "product.description", target = "description")
    @Mapping(source = "product.image", target = "image")
    @Mapping(source = "product.specsId", target = "productSpecs.id")
    @Mapping(source = "productSpecs", target = "productSpecs")
    ProductResponseDTO toProductResponseDTO(Product product, ProductSpecs productSpecs);
}
