package org.example.ecommerce.dtos.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreditCardDTO {
    private Long id;
    @JsonProperty("masked_pan")
    private String maskedPan;
}
