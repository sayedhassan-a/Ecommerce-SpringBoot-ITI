package org.example.ecommerce.controllers;

import org.example.ecommerce.dtos.OrderResponseDTO;
import org.example.ecommerce.dtos.OrderRequestDTO;
import org.example.ecommerce.dtos.OrderViewDTO;
import org.example.ecommerce.dtos.OrderWithItemsDTO;
import org.example.ecommerce.models.Customer;
import org.example.ecommerce.models.OrderState;
import org.example.ecommerce.models.PaymentMethod;
import org.example.ecommerce.services.CustomerService;
import org.example.ecommerce.services.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;
    private final CustomerService customerService;
    public OrderController(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @GetMapping("/admin")
    public ResponseEntity<Page<OrderViewDTO>> findAllForAdmin(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) List<OrderState> states,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) List<PaymentMethod> paymentMethods) {

        Map<String, Object> map = new HashMap<>();
        if(size != null)map.put("states", states);
        if(startDate != null)map.put("startDate", startDate);
        if(endDate != null)map.put("endDate", endDate);
        if(minPrice != null)map.put("minPrice", minPrice);
        if(maxPrice != null)map.put("maxPrice", maxPrice);
        if(customerId != null)map.put("customerId", customerId);
        if(paymentMethods != null)map.put("paymentMethods", paymentMethods);

        return ResponseEntity.ok(orderService.findAllBySpecs(page, size, map));

    }

    @GetMapping("/customer")
    public ResponseEntity<Page<OrderViewDTO>> findAll(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) List<OrderState> states,
            @RequestParam(required = false) List<PaymentMethod> paymentMethods) {

        Map<String, Object> map = new HashMap<>();
        if(size != null)map.put("states", states);
        if(startDate != null)map.put("startDate", startDate);
        if(endDate != null)map.put("endDate", endDate);
        if(minPrice != null)map.put("minPrice", minPrice);
        if(maxPrice != null)map.put("maxPrice", maxPrice);
        if(paymentMethods != null)map.put("paymentMethods", paymentMethods);
        Customer customer =
                customerService.findUserByEmail(
                        (String) ((Jwt) SecurityContextHolder.getContext()
                                .getAuthentication().getPrincipal()).getClaims().get(
                                "sub"));
        map.put("customerId",customer.getId()); 
        return ResponseEntity.ok(orderService.findAllBySpecs(page, size, map));

    }
    @GetMapping("/admin/{id}")
    public ResponseEntity<OrderWithItemsDTO> findOrderForAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findOrderWithId(id));
    }
    @GetMapping("/customer/{id}")
    public ResponseEntity<OrderWithItemsDTO> findOrderForCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findOrderWithIdForCustomer(id));
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(
            @RequestBody OrderRequestDTO orderRequestDTO){

        return ResponseEntity.ok(orderService.initOrder(
                orderRequestDTO).orElse(null));
    }
}
