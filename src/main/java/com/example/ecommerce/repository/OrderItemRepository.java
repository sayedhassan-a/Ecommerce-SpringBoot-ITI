package com.example.ecommerce.repository;

import com.example.ecommerce.entity.OrderItem;
import com.example.ecommerce.entity.OrderItemID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemID> {}
