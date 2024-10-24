package org.example.ecommerce.controllers;

import org.example.ecommerce.dtos.OrderResponseDTO;
import org.example.ecommerce.dtos.OrderRequestDTO;
import org.example.ecommerce.models.OrderState;
import org.example.ecommerce.services.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/orders")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> findAll(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) OrderState state,
            @RequestParam(required = false) Long customerId) {

        Map<String, Object> map = new HashMap<>();
        if(size != null)map.put("state", state);
        if(startDate != null)map.put("startDate", startDate);
        if(endDate != null)map.put("endDate", endDate);
        if(minPrice != null)map.put("minPrice", minPrice);
        if(maxPrice != null)map.put("maxPrice", maxPrice);
        if(customerId != null)map.put("customerId", customerId);

        return ResponseEntity.ok(orderService.findAllBySpecs(page, size, map));

    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(
            @RequestBody OrderRequestDTO orderRequestDTO){
        Long customerId = null;
        return ResponseEntity.ok(orderService.initOrder(customerId,
                orderRequestDTO).orElse(null));
    }
}
