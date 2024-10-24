package org.example.ecommerce.services;

import org.example.ecommerce.models.Order;
import org.example.ecommerce.models.OrderState;
import org.example.ecommerce.models.PaymentMethod;
import org.example.ecommerce.repositories.OrderRepository;
import org.example.ecommerce.specifications.OrderSpecs;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CancelOrderService {
    private final OrderRepository orderRepository;

    public CancelOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    @Scheduled(fixedRateString = "${order.scheduler.fixedRate}")
    public void cancelOrder() {

        List<Order> orders =
                orderRepository.findAll(OrderSpecs
                        .hasDateBefore(LocalDateTime.now().minusMinutes(15))
                        .and(OrderSpecs.hasState(OrderState.PLACED))
                        .and(OrderSpecs.hasPaymentMethod(PaymentMethod.CREDIT_CARD)));

        orders.forEach(order -> {
            order.setState(OrderState.CANCELLED);

            order.getOrderItems().stream().parallel().forEach(item -> {
                item.getProduct().setStock(
                        item.getProduct().getStock() + item.getQuantity()
                );
            });

        });
    }
}
