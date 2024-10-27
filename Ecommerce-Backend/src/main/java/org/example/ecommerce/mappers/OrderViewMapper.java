package org.example.ecommerce.mappers;

import org.example.ecommerce.dtos.OrderViewDTO;
import org.example.ecommerce.models.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderViewMapper {
    @Mapping(source = "customer.id", target = "customerId")
    OrderViewDTO toDTO(Order order);
}
