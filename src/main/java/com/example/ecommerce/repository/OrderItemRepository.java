package com.example.ecommerce.repository;

import com.example.ecommerce.entity.OrderItem;
import com.example.ecommerce.entity.OrderItemID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemID> {}
