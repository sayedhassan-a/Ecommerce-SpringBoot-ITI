package org.example.ecommerce.controllers;

import jakarta.validation.Valid;
import org.example.ecommerce.dtos.CustomerDto;
import org.example.ecommerce.dtos.customerConverters.CustomerDtoToCustomerConverter;
import org.example.ecommerce.dtos.customerConverters.CustomerToCustomerDtoConverter;
import org.example.ecommerce.models.Customer;
import org.example.ecommerce.models.OAuthProvider;
import org.example.ecommerce.security.GoogleTokenValidator;
import org.example.ecommerce.services.AuthService;
import org.example.ecommerce.services.CustomerService;
import org.example.ecommerce.system.Result;
import org.example.ecommerce.system.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthService authService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationManager authenticationManager;
    private final GoogleTokenValidator googleTokenValidator;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, GoogleTokenValidator googleTokenValidator) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.googleTokenValidator = googleTokenValidator;
    }

    @PostMapping
    public Result getLoginInfo(@RequestBody Customer customer) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        customer.getEmail(), customer.getPassword()));

        LOGGER.debug("Authenticated user: {}", authentication.getName());

        return new Result(true, StatusCode.SUCCESS, "User Info and Json Web Token", this.authService.createLoginInfo(authentication));
    }

    @GetMapping("/validate-token")
    public Result validateToken() {
        System.out.println("Validating token");
        return new Result(true, StatusCode.SUCCESS, "User Info and Json Web " +
                "Token", this.authService.validateToken());

    }
}
