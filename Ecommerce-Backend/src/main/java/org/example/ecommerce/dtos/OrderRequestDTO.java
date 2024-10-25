package org.example.ecommerce.dtos;

import lombok.Getter;
import lombok.Setter;
import org.example.ecommerce.models.PaymentMethod;

@Getter
@Setter
public class OrderRequestDTO {
    private PaymentMethod paymentMethod;
}
