package org.example.ecommerce.dtos;

public record AddressDto(
    String AddressOne,
    String City,
    String Country,
    String ZipCode
) {}