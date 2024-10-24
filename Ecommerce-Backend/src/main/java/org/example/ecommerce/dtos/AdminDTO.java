package org.example.ecommerce.dtos;

import jakarta.validation.constraints.NotEmpty;
import org.example.ecommerce.models.Role;

public record AdminDTO(
    Long id,
    String firstName,
    String middleName,
    String lastName,
    @NotEmpty(message = "Email is required.")
    String email,
    Role role
) {}