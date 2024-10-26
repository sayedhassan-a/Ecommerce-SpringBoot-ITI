package org.example.ecommerce.dtos.payment.token;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenDTO {
    private String type;
    private TokenBodyDTO obj;
}
