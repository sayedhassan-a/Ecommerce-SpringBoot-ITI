package org.example.ecommerce.controllers;

import org.example.ecommerce.dtos.payment.token.TokenDTO;
import org.example.ecommerce.dtos.payment.transaction.TransactionDTO;
import org.example.ecommerce.services.PaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public void updateOrderStatus(@RequestBody TransactionDTO transactionDTO) {
        System.out.println("PaymentController.updateOrderStatus");
        paymentService.handleTransactionStatus(transactionDTO);
    }
    @PostMapping("token")
    public void saveToken(@RequestBody TokenDTO tokenDTO) {
        paymentService.saveToken(tokenDTO);
    }
}
