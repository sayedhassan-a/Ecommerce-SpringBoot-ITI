package org.example.ecommerce.services;

import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import org.example.ecommerce.dtos.payment.PaymentDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentService {

    @Value("${payment-secret-key}")
    private String PAYMENT_SECRET_KEY;

    @Value("${payment-public-key}")
    private String PAYMENT_PUBLIC_KEY;

    @Value("${payment-intention-url")
    private String PAYMENT_INTENTION_URL;

    @Value("${payment-checkout-url")
    private String PAYMENT_CHECKOUT_URL;

    private final RestTemplate restTemplate;

    public PaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String generateLink(PaymentDTO paymentDTO){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", PAYMENT_SECRET_KEY);
        headers.set("Content-Type", "application/json");

        HttpEntity<PaymentDTO> httpEntity = new HttpEntity<>(paymentDTO, headers);

        JsonObject response = JsonParser.parseString(
                restTemplate.exchange(PAYMENT_INTENTION_URL, HttpMethod.POST,
                httpEntity, String.class).toString()).getAsJsonObject();

        String clientSecret = response.get("client_secret").getAsString();

        return PAYMENT_CHECKOUT_URL
                + "?" + "publicKey=" + PAYMENT_PUBLIC_KEY
                + "&" + "clientSecret=" + clientSecret;

    }
}
