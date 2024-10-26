package org.example.ecommerce.dtos.payment.transaction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDTO {
    private String type;
    private TransactionBodyDTO obj;
}
