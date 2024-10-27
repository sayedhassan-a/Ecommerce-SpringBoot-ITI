package org.example.ecommerce.dtos;

import lombok.Getter;
import lombok.Setter;
import org.example.ecommerce.models.OrderItem;

import java.util.List;

@Getter
@Setter
public class OrderWithItemsDTO {
    OrderViewDTO order;
    List<ProductCartDTO> productCartDTOS;
}
