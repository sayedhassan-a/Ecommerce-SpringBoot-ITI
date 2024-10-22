package org.example.ecommerce.dtos;

import jakarta.validation.constraints.NotEmpty;

public record AdminDTO(
    Long id,
    String firstName,
    String middleName,
    String lastName,
    @NotEmpty(message = "Email is required.")
    String email
) {}