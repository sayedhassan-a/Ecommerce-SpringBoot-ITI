package org.example.ecommerce.dtos;

import org.example.ecommerce.models.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserDto(
    Long id,
    String firstName,
    String middleName,
    String lastName,
    String email,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Role role
) {}