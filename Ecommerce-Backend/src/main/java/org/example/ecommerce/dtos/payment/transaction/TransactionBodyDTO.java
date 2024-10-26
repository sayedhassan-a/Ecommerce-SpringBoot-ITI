package org.example.ecommerce.dtos.payment.transaction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionBodyDTO {
    private boolean success;
    private PaymentClaims payment_key_claims;
}
