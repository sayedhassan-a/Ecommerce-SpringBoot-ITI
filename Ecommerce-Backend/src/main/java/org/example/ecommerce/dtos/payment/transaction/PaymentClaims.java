package org.example.ecommerce.dtos.payment.transaction;

import lombok.Getter;
import lombok.Setter;
import org.example.ecommerce.dtos.payment.ExtrasDTO;

@Getter
@Setter
public class PaymentClaims {
    private ExtrasDTO extra;
    private Long order_id;
}
