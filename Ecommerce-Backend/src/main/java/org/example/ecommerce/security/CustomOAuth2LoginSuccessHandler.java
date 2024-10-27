package org.example.ecommerce.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.ecommerce.dtos.UserDto;
import org.example.ecommerce.mappers.UserMapper;
import org.example.ecommerce.models.OAuthProvider;
import org.example.ecommerce.models.User;
import org.example.ecommerce.services.AuthService;
import org.example.ecommerce.services.MyUserPrincipal;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService; // Inject your AuthService
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final JwtProvider jwtProvider;
    private final UserMapper userMapper;

    public CustomOAuth2LoginSuccessHandler(@Lazy AuthService authService, OAuth2AuthorizedClientService authorizedClientService, @Lazy JwtProvider jwtProvider, UserMapper userMapper) {
        this.authService = authService;
        this.authorizedClientService = authorizedClientService;
        this.jwtProvider = jwtProvider;
        this.userMapper = userMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        try {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            OAuth2User principal = oauthToken.getPrincipal();

            OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                    oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());

            if (authorizedClient != null) {
                String accessToken = authorizedClient.getAccessToken().getTokenValue();
                System.out.println("Access Token: " + accessToken); // Debugging
            }

            // Get the JWT token from the auth service after login or registration
            String jwtToken = authService.handleOAuthLoginOrRegister(principal, OAuthProvider.GOOGLE);

            // Return the JWT and status message to the client
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"token\": \"" + jwtToken + "\", \"message\": \"Authentication successful\"}");
        } catch (Exception e) {
            // Log error and respond with JSON error message
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Authentication failed\"}");
        }
    }
}
