package org.example.ecommerce.dtos;

import lombok.Getter;
import lombok.Setter;
import org.example.ecommerce.models.OrderState;
import org.example.ecommerce.models.PaymentMethod;

import java.time.LocalDate;

@Getter
@Setter
public class OrderResponseDTO {
    private Long id;
    private LocalDate date = LocalDate.now();
    private int totalPrice;
    private OrderState state;
    private PaymentMethod paymentMethod;
    private String paymentLink = null;
}
