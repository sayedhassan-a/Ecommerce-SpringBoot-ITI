package org.example.ecommerce.dtos.payment.token;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenBodyDTO {
    private String token;
    private String email;
}
