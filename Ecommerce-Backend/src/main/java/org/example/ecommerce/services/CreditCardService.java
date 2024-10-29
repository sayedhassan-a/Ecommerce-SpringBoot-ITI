package org.example.ecommerce.services;

import jakarta.transaction.Transactional;
import org.example.ecommerce.dtos.payment.CreditCardDTO;
import org.example.ecommerce.models.CreditCard;
import org.example.ecommerce.models.Customer;
import org.example.ecommerce.repositories.CreditCardRepository;
import org.example.ecommerce.repositories.CustomerRepository;
import org.example.ecommerce.system.exceptions.exceptionHandling.exception.NotFoundException;
import org.example.ecommerce.system.exceptions.exceptionHandling.exception.UnAuthorizedAccessException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditCardService {
    private final CustomerService customerService;
    private final CreditCardRepository creditCardRepository;

    public CreditCardService(CustomerService customerService, CreditCardRepository creditCardRepository) {
        this.customerService = customerService;
        this.creditCardRepository = creditCardRepository;
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

    @Transactional
    public void deleteById(Long id){
        String email = (String) ((Jwt) SecurityContextHolder.getContext()
                                .getAuthentication().getPrincipal()).getClaims().get(
                                "sub");

        CreditCard creditCard =
                creditCardRepository.findById(id).orElseThrow(()->new NotFoundException("No credit card with this id"));
        if(creditCard.getCustomer().getEmail().equals(email)){
            creditCardRepository.deleteById(id);
            return;
        }
        throw new UnAuthorizedAccessException("User is not authorized to " +
                "delete this credit card");
    }
}
