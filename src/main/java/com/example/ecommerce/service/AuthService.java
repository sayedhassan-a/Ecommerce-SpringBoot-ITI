package com.example.ecommerce.service;

import com.example.ecommerce.dto.AuthRequest;
import com.example.ecommerce.dto.CustomerDTO;
import com.example.ecommerce.dto.RegistrationRequest;
import com.example.ecommerce.dto.UpdatePasswordRequest;
import com.example.ecommerce.entity.Customer;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.security.JwtService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {
    private final CustomerRepository customerRepository;

    public AuthService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerDTO register(RegistrationRequest request) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<RegistrationRequest>> violations =
                validator.validate(request);
        ///Handle Exception
        if(!violations.isEmpty()){
            violations.forEach(violation -> System.out.println(violation.getMessage()));
            return null;
        }
        Customer customer = new Customer();
        customer.setEmail(request.getEmail());
        customer.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setActive(true);
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setJob(request.getJob());
        customer.setInterests(request.getInterests());
        Customer saved = customerRepository.save(customer);
        if(saved == null){
            return null;
        }

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(saved.getId());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setPhone(customer.getPhone());
        customerDTO.setAddress(customer.getAddress());
        return customerDTO;
    }

    public CustomerDTO updatePassword(Customer customer,
                                      UpdatePasswordRequest request) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<UpdatePasswordRequest>> violations =
                validator.validate(request);

        if(!violations.isEmpty()){
            violations.forEach(violation -> System.out.println(violation.getMessage()));
            return null;
        }
        customer.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        Customer saved = customerRepository.save(customer);

        if(saved == null){
            return null;
        }

        return new CustomerDTO(saved);
    }

    public String loginWithToken(AuthRequest request) {
        try {
            Customer customer = customerRepository.findCustomerByEmail(request.getEmail());

            if (BCrypt.checkpw(request.getPassword(), customer.getPassword())) {
                String token = JwtService.generate(customer.getId(),
                        "CUSTOMER");
                System.out.println(token);

                return token;
            } else
                return null;
        } catch (Exception e) {
            return null;
        }
    }
    public boolean login(AuthRequest request) {
        try {

            Customer customer = customerRepository.findCustomerByEmail(request.getEmail());
            return BCrypt.checkpw(request.getPassword(), customer.getPassword());

        } catch (Exception e) {
            return false;
        }

    }

}
