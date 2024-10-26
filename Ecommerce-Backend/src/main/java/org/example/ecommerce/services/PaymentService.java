package org.example.ecommerce.services;

import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import org.example.ecommerce.dtos.payment.PaymentDTO;
import org.example.ecommerce.dtos.payment.token.TokenDTO;
import org.example.ecommerce.dtos.payment.transaction.TransactionDTO;
import org.example.ecommerce.models.CreditCard;
import org.example.ecommerce.models.Customer;
import org.example.ecommerce.models.OrderState;
import org.example.ecommerce.repositories.CreditCardRepository;
import org.example.ecommerce.repositories.CustomerRepository;
import org.example.ecommerce.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentService {

    @Value("${payment-secret-key}")
    private String PAYMENT_SECRET_KEY;

    @Value("${payment-public-key}")
    private String PAYMENT_PUBLIC_KEY;

    @Value("${payment-intention-url}")
    private String PAYMENT_INTENTION_URL;

    @Value("${payment-checkout-url}")
    private String PAYMENT_CHECKOUT_URL;

    private final RestTemplate restTemplate;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final CreditCardRepository creditCardRepository;

    public PaymentService(RestTemplate restTemplate, OrderRepository orderRepository,
                          CustomerRepository customerRepository, CreditCardRepository creditCardRepository) {
        this.restTemplate = restTemplate;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.creditCardRepository = creditCardRepository;
    }

    public String generateLink(PaymentDTO paymentDTO){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", PAYMENT_SECRET_KEY);
        headers.set("Content-Type", "application/json");

        HttpEntity<PaymentDTO> httpEntity = new HttpEntity<>(paymentDTO, headers);
        System.out.println(PAYMENT_INTENTION_URL);

        ResponseEntity<String> responseEntity=null;
        for(int i=0;i<5 && (responseEntity==null || responseEntity.getBody() == null);i++){
            responseEntity = restTemplate.exchange(
                    PAYMENT_INTENTION_URL,
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );
        }


        JsonObject response = JsonParser.parseString(responseEntity.getBody()).getAsJsonObject();
        String clientSecret = response.get("client_secret").getAsString();

        return PAYMENT_CHECKOUT_URL
                + "?" + "publicKey=" + PAYMENT_PUBLIC_KEY
                + "&" + "clientSecret=" + clientSecret;

    }

    @Transactional
    public void handleTransactionStatus(TransactionDTO transactionDTO){
        Long orderId =
                transactionDTO.getObj().getPayment_key_claims().getExtra().getOrder_id();

        boolean status = transactionDTO.getObj().isSuccess();

        if(status){
            orderRepository.findById(orderId).ifPresent(order -> {order.setState(OrderState.CONFIRMED);});
        }
    }

    @Transactional
    public void saveToken(TokenDTO tokenDTO){
        CreditCard creditCard = new CreditCard();
        creditCard.setToken(tokenDTO.getObj().getToken());
        Customer customer = customerRepository
                .findByEmail(tokenDTO.getObj().getEmail())
                .orElseThrow(RuntimeException::new);
        if(customer.getCreditCard().size()>=5)return;
        creditCard.setCustomer(customer);
        creditCardRepository.save(creditCard);
    }
}
