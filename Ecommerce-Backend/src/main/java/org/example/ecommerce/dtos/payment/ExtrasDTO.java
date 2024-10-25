package org.example.ecommerce.dtos.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExtrasDTO {
    private Long order_id;
    public ExtrasDTO(Long order_id) {
        this.order_id = order_id;
    }
}
