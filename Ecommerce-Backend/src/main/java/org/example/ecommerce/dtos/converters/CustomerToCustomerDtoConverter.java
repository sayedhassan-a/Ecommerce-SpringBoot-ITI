package org.example.ecommerce.dtos.converters;

import org.example.ecommerce.dtos.CustomerDto;
import org.example.ecommerce.models.Customer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CustomerToCustomerDtoConverter implements Converter<Customer, CustomerDto> {
    @Override
    public CustomerDto convert(Customer source) {
        // build the new object with the same sequence of fields in CustomerDto
        return new CustomerDto(source.getId(), source.getEmail(), source.getFirstName(),
                source.getMiddleName(), source.getLastName(), source.getPhone(), source.getAddress(),
                source.getDateOfBirth(), source.isActive());
    }
}