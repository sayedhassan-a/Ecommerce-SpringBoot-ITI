package com.example.ecommerce.service;


import com.example.ecommerce.entity.Customer;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.enums.OrderState;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    Order addOrder(Customer customer, String coupon);
    Order getOrder(int id);
    Page<Order> getOrders(int page, int size);
    Order updateOrderState(int id, OrderState state);
    public List<Order> getOrdersByCustomerId(int customerId);
    public void deleteOrder(int id);
}
