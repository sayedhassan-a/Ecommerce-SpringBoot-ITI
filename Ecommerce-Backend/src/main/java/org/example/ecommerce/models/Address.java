package org.example.ecommerce.models;

import jakarta.persistence.*;
import org.hibernate.annotations.SoftDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@SoftDelete
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String AddressOne;
    private String AddressTwo;
    private String City;
    private String Country;
    private String ZipCode;

    @OneToOne(mappedBy = "address")
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
