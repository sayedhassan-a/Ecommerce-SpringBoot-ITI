package org.example.ecommerce.services;

import org.example.ecommerce.dtos.payment.CreditCardDTO;
import org.example.ecommerce.models.CreditCard;
import org.example.ecommerce.models.Customer;
import org.example.ecommerce.repositories.CreditCardRepository;
import org.example.ecommerce.repositories.CustomerRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditCardService {
    private final CustomerService customerService;

    public CreditCardService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public List<CreditCardDTO> findAll() {
        //Retrieve current customer
        Customer customer =
                customerService.findUserByEmail(
                        (String) ((Jwt) SecurityContextHolder.getContext()
                                .getAuthentication().getPrincipal()).getClaims().get(
                                "sub"));

        return customer.getCreditCard().stream().map((creditCard ->
        {
            CreditCardDTO creditCardDTO = new CreditCardDTO();
            creditCardDTO.setId(creditCard.getId());
            creditCardDTO.setMaskedPan(creditCard.getMaskedPan());
            return creditCardDTO;
        })).toList();
    }
}
