package org.example.ecommerce.controllers;

import jakarta.validation.Valid;
import org.example.ecommerce.dtos.CustomerDto;
import org.example.ecommerce.dtos.converters.CustomerDtoToCustomerConverter;
import org.example.ecommerce.dtos.converters.CustomerToCustomerDtoConverter;
import org.example.ecommerce.models.Customer;
import org.example.ecommerce.services.CustomerService;
import org.example.ecommerce.system.Result;
import org.example.ecommerce.system.StatusCode;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("${api.endpoint.base-url}/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerDtoToCustomerConverter customerDtoToCustomerConverter;
    private final CustomerToCustomerDtoConverter customerToCustomerDtoConverter;

    public CustomerController(CustomerService customerService,
                              CustomerDtoToCustomerConverter customerDtoToCustomerConverter,
                              CustomerToCustomerDtoConverter customerToCustomerDtoConverter) {
        this.customerService = customerService;
        this.customerDtoToCustomerConverter = customerDtoToCustomerConverter;
        this.customerToCustomerDtoConverter = customerToCustomerDtoConverter;
    }

    // Create a new customer
    @PostMapping
    public Result createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        // Convert DTO to entity and save
        Customer customer = customerDtoToCustomerConverter.convert(customerDto);
        Customer savedCustomer = customerService.save(customer);
        // Convert saved entity back to DTO
        CustomerDto savedCustomerDto = customerToCustomerDtoConverter.convert(savedCustomer);
        return new Result(true, StatusCode.SUCCESS, "Customer created successfully", savedCustomerDto);
    }

    // Retrieve all customers
    @GetMapping
    public Result findAllCustomers() {
        List<Customer> customers = customerService.findAll();
        // Convert list of entities to DTOs
        List<CustomerDto> customerDtos = customers.stream()
                .map(customerToCustomerDtoConverter::convert)
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Customers retrieved successfully", customerDtos);
    }

    // Retrieve customer by ID
    @GetMapping("/{id}")
    public Result findCustomerById(@PathVariable Long id) {
        Customer foundCustomer = customerService.findById(id);
        CustomerDto customerDto = customerToCustomerDtoConverter.convert(foundCustomer);
        return new Result(true, StatusCode.SUCCESS, "Customer retrieved successfully", customerDto);
    }

    // Retrieve customer by email
    @GetMapping("/email")
    public Result findCustomerByEmail(@RequestParam String email) {
        Customer foundCustomer = customerService.findUserByEmail(email);
        CustomerDto customerDto = customerToCustomerDtoConverter.convert(foundCustomer);
        return new Result(true, StatusCode.SUCCESS, "Customer retrieved successfully", customerDto);
    }

    // Update an existing customer
    @PutMapping("/{id}")
    public Result updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerDto customerDto) {
        // Convert DTO to entity for update
        Customer customer = customerDtoToCustomerConverter.convert(customerDto);
        Customer updatedCustomer = customerService.update(id, customer);
        // Convert updated entity back to DTO
        CustomerDto updatedCustomerDto = customerToCustomerDtoConverter.convert(updatedCustomer);
        return new Result(true, StatusCode.SUCCESS, "Customer updated successfully", updatedCustomerDto);
    }

    // Delete a customer
    @DeleteMapping("/{id}")
    public Result deleteCustomer(@PathVariable Long id) {
        customerService.delete(id);
        return new Result(true, StatusCode.SUCCESS, "Customer deleted successfully", null);
    }
}