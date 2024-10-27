package org.example.ecommerce.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "credit_card")
@Getter
@Setter
@NoArgsConstructor
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private String maskedPan;

    @ManyToOne
    private Customer customer;
}
