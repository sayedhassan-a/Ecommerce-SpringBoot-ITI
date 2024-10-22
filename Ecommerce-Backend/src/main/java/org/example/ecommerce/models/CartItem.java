package org.example.ecommerce.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cart")
@Setter
@Getter
@NoArgsConstructor
@IdClass(CartKey.class)
public class CartItem {

    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    @Id
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    @Id
    private Product product;
}