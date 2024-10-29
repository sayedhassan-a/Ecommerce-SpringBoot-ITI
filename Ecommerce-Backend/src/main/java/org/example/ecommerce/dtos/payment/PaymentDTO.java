package org.example.ecommerce.dtos.payment;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ecommerce.models.CreditCard;
import org.example.ecommerce.models.Customer;
import org.example.ecommerce.models.Order;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PaymentDTO {
    private int amount;
    private String currency;
    private String notification_url;
    private List<String> card_tokens;
    private int expiration;
    private List<Object> payment_methods = new ArrayList<>();
    private BillingDataDTO billing_data;
    private CustomerPaymentDTO customer;
    private ExtrasDTO extras;

    public PaymentDTO(Customer customer, Order order) {
        this.customer = new CustomerPaymentDTO(customer);
        this.extras = new ExtrasDTO(order.getId());
        this.billing_data = new BillingDataDTO(customer, order);
        this.amount = order.getTotalPrice();
        this.currency = "EGP";
        this.expiration = 60*15;
        this.payment_methods.add(4719614);
        this.card_tokens =
                customer.getCreditCard().stream()
                        .map(CreditCard::getToken).toList();
    }

}
