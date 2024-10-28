package org.example.ecommerce.mappers;


import org.example.ecommerce.dtos.ProductResponseDTO;
import org.example.ecommerce.models.Product;
import org.example.ecommerce.specifications.ProductSpecs;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mappings({
            @Mapping(source = "product.id", target = "id"),
            @Mapping(source = "product.name", target = "name"),
            @Mapping(source = "product.price", target = "price"),
            @Mapping(source = "product.description", target = "description"),
            @Mapping(source = "product.image", target = "image"),
            @Mapping(source = "product.images", target = "productImages"),
            @Mapping(source = "product.specsId", target = "productSpecs.id"),
            @Mapping(source = "productSpecs", target = "productSpecs"),
            @Mapping(source = "product.subCategory.name", target = "categoryName"),
            @Mapping(source = "product.subCategory.id", target = "subCategoryId"),
            @Mapping(source = "product.brandName", target = "brandName"),
            @Mapping(source = "product.salePercentage", target = "salePercentage"),
            @Mapping(source = "product.stock", target = "stock")
    })
    ProductResponseDTO toProductResponseDTO(Product product, ProductSpecs productSpecs);


}
