package org.example.ecommerce.dtos;

import jakarta.validation.constraints.NotEmpty;
import org.example.ecommerce.models.Address;

import java.time.LocalDate;

public record CustomerDto(Long Id,

                          @NotEmpty(message = "Email is required.")
                          String email,
                          String firstName,
                          String middleName,
                          String lastName,
                          String phone,
                          Address address,
                          LocalDate dateOfBirth,
                          boolean isActive) {
}
