package org.example.ecommerce.dtos;

import lombok.Getter;
import lombok.Setter;
import org.example.ecommerce.models.OrderState;
import org.example.ecommerce.models.PaymentMethod;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderViewDTO {
    private Long customerId;
    private Long id;
    private LocalDateTime date = LocalDateTime.now();
    private int totalPrice;
    private OrderState state;
    private PaymentMethod paymentMethod;
    private String paymentLink = null;
}
