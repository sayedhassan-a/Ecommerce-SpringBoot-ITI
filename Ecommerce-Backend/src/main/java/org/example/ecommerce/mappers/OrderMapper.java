package org.example.ecommerce.mappers;

import org.example.ecommerce.dtos.OrderResponseDTO;
import org.example.ecommerce.models.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper extends GenericMapper<Order, OrderResponseDTO> {
}
