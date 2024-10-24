package org.example.ecommerce.dtos.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ecommerce.models.Customer;

@Getter
@Setter
@NoArgsConstructor
public class CustomerPaymentDTO {
    private String first_name;
    private String last_name;
    private String email;

    public CustomerPaymentDTO(Customer customer) {
        this.first_name = customer.getFirstName();
        this.last_name = customer.getLastName();
        this.email = customer.getEmail();

    }
}
