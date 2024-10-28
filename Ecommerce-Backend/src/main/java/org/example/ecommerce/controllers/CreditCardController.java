package org.example.ecommerce.controllers;

import org.example.ecommerce.dtos.payment.CreditCardDTO;
import org.example.ecommerce.services.CreditCardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/credit-card")
public class CreditCardController {
    private final CreditCardService creditCardService;

    public CreditCardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }
    @GetMapping
    public ResponseEntity<List<CreditCardDTO>> findAll(){
        return ResponseEntity.ok(creditCardService.findAll());
    }
}
