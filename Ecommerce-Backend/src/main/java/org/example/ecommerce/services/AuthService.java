package org.example.ecommerce.services;

import org.antlr.v4.runtime.Token;
import org.example.ecommerce.controllers.AuthController;
import org.example.ecommerce.dtos.CustomerDto;
import org.example.ecommerce.dtos.UserDto;
import org.example.ecommerce.mappers.UserMapper;
import org.example.ecommerce.models.Customer;
import org.example.ecommerce.models.OAuthProvider;
import org.example.ecommerce.models.Role;
import org.example.ecommerce.models.User;
import org.example.ecommerce.repositories.CustomerRepository;
import org.example.ecommerce.security.JwtProvider;
import org.example.ecommerce.system.exceptions.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final JwtProvider jwtProvider;
    private final UserMapper userMapper;
    private final CustomerRepository customerRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);


    public AuthService(JwtProvider jwtProvider, UserMapper userMapper, CustomerRepository customerRepository) {
        this.jwtProvider = jwtProvider;
        this.userMapper = userMapper;
        this.customerRepository = customerRepository;
    }

    public void registerOAuthUser(Customer customer, OAuthProvider provider) {
        customerRepository.save(customer);
    }
    public String handleOAuthLoginOrRegister(OAuth2User principal, OAuthProvider provider) {
        System.out.println(principal.getAttributes());

        String email = principal.getAttribute("email");

        if (provider.equals(OAuthProvider.GOOGLE)) {
            String firstName = principal.getAttribute("given_name");
            String lastName = principal.getAttribute("family_name");

            // Check if customer exists
            Optional<Customer> existingCustomer = customerRepository.findByEmail(email);

            if (existingCustomer.isPresent()) {
                // Existing customer: proceed with login
                LOGGER.debug("Customer logged in with {}", provider);
                return jwtProvider.createToken(userMapper.toDTO(existingCustomer.get()));
            } else {
                // New customer: register them
                Customer newCustomer = new Customer();
                newCustomer.setEmail(email);
                newCustomer.setFirstName(firstName);
                newCustomer.setLastName(lastName);
                newCustomer.setRole(Role.ROLE_USER);
                newCustomer.setProvider(provider);

                registerOAuthUser(newCustomer, provider);  // Persist the new customer
                LOGGER.debug("Customer registered with {}", provider);

                return jwtProvider.createToken(userMapper.toDTO(newCustomer)); // Generate token for new customer
            }
        }
        return null; // or throw an exception if the provider is invalid
    }

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        MyUserPrincipal userPrincipal = (MyUserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();
        System.out.println(user);
        UserDto userDto = userMapper.toDTO(user);
        String token = this.jwtProvider.createToken(userDto);

        return Map.of(
                "userInfo", userDto,
                "token", token
        );
    }
}