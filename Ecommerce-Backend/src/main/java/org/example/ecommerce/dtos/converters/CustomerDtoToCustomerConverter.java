package org.example.ecommerce.dtos.converters;

import org.example.ecommerce.dtos.CustomerDto;
import org.example.ecommerce.models.Customer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CustomerDtoToCustomerConverter implements Converter<CustomerDto, Customer> {
    @Override
    public Customer convert(CustomerDto source) {
        Customer customer = new Customer();
        customer.setId(source.Id());
        customer.setEmail(source.email());
        customer.setFirstName(source.firstName());
        customer.setMiddleName(source.middleName());
        customer.setLastName(source.lastName());
        customer.setPhone(source.phone());
        customer.setAddress(source.address());
        customer.setDateOfBirth(source.dateOfBirth());
        customer.setActive(source.isActive());

        return customer;
    }
}