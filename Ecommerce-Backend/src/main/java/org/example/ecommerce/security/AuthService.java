package org.example.ecommerce.security;

import org.example.ecommerce.dtos.CustomerDto;
import org.example.ecommerce.dtos.customerConverters.CustomerToCustomerDtoConverter;
import org.example.ecommerce.models.Customer;
import org.example.ecommerce.models.User;
import org.example.ecommerce.services.MyUserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final JwtProvider jwtProvider;

    private final CustomerToCustomerDtoConverter customerToCustomerDtoConverter;

    public AuthService(JwtProvider jwtProvider, CustomerToCustomerDtoConverter customerToCustomerDtoConverter) {
        this.jwtProvider = jwtProvider;
        this.customerToCustomerDtoConverter = customerToCustomerDtoConverter;
    }

    public Map<String, Object> creatLoginInfo(Authentication authentication) {
        // Create User Info
        MyUserPrincipal userPrincipal = (MyUserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();

        CustomerDto customerDto = null;
        String token = "";

        if (user instanceof Customer) {
            Customer customer = (Customer) user;
            customerDto = customerToCustomerDtoConverter.convert(customer);

            // Create JWT
            token = this.jwtProvider.createToken(authentication);
        }

        Map<String, Object> loginResultMap = Map.of(
                "customerInfo", customerDto,
                "token", token
        );

        return loginResultMap;
    }
}
