package org.example.ecommerce.models;

import lombok.*;

import java.io.Serializable;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CartKey implements Serializable {
    private Customer customer;
    private Product product;
}