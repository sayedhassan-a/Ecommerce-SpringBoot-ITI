package org.example.ecommerce.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.ecommerce.dtos.CustomerDto;
import org.example.ecommerce.dtos.customerConverters.CustomerDtoToCustomerConverter;
import org.example.ecommerce.dtos.customerConverters.CustomerToCustomerDtoConverter;
import org.example.ecommerce.models.Customer;
import org.example.ecommerce.services.AuthService;
import org.example.ecommerce.services.CustomerService;
import org.example.ecommerce.system.Result;
import org.example.ecommerce.system.StatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerDtoToCustomerConverter customerDtoToCustomerConverter;
    private final CustomerToCustomerDtoConverter customerToCustomerDtoConverter;
    private final AuthService authService;

    public CustomerController(CustomerService customerService,
                              CustomerDtoToCustomerConverter customerDtoToCustomerConverter,
                              CustomerToCustomerDtoConverter customerToCustomerDtoConverter, AuthService authService) {
        this.customerService = customerService;
        this.customerDtoToCustomerConverter = customerDtoToCustomerConverter;
        this.customerToCustomerDtoConverter = customerToCustomerDtoConverter;
        this.authService = authService;
    }

    // Create a new customer
    @PostMapping("/register")
    public Result createCustomer(@Valid @RequestBody Customer customer) {
        System.out.println("inside createCustomer: " + customer);
        Customer savedCustomer = customerService.save(customer);
        // Convert saved entity back to DTO
        CustomerDto savedCustomerDto = customerToCustomerDtoConverter.convert(savedCustomer);
        return new Result(true, StatusCode.SUCCESS, "Customer created successfully", savedCustomerDto);
    }
//    Customer customer =
//            customerService.findUserByEmail(
//                    (String) ((Jwt)SecurityContextHolder.getContext()
//                            .getAuthentication().getPrincipal()).getClaims().get(
//                            "sub"));
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

    @GetMapping("/checkEmail")
    public ResponseEntity<String> checkEmail(@RequestParam String email) {
        boolean exists = customerService.existsByEmail(email);
        return ResponseEntity.ok(Boolean.toString(!exists)); // Return "true" if email is available, "false" if not
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

    @GetMapping("/profile")
    public ResponseEntity<CustomerDto> getCustomerProfile() {

        System.out.println("in here " + "       ");
        String email = authService.extractEmail(); // Extract email from JWT token

        System.out.println("in here " + "       " + email);
        Customer foundCustomer = customerService.findUserByEmail(email);
        CustomerDto customerDto = customerToCustomerDtoConverter.convert(foundCustomer);
        System.out.println("in here " + "       " + customerDto);
        return ResponseEntity.ok(customerDto);
    }

    @PutMapping("/profile")
    public Result updateCustomer(@RequestBody Customer customer) {
        // Extract email from JWT
        String email = authService.extractEmail();

        System.out.println("in here " + "       " + email);
        // Update customer by email
        Customer updatedCustomer = customerService.updateByEmail(email, customer);

        // Convert updated entity back to DTO
        CustomerDto updatedCustomerDto = customerToCustomerDtoConverter.convert(updatedCustomer);
        return new Result(true, StatusCode.SUCCESS, "Customer updated successfully", updatedCustomerDto);
    }

}