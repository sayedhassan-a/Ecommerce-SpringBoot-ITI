package org.example.ecommerce.security;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.JwtException;

import java.util.Map;

@Service
public class GoogleTokenValidator {

    private static final String GOOGLE_USERINFO_ENDPOINT = "https://www.googleapis.com/oauth2/v3/userinfo";

    public Map validateAccessToken(String accessToken) {
        try {
            return WebClient.create()
                .get()
                .uri(GOOGLE_USERINFO_ENDPOINT)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        } catch (Exception ex) {
            throw new JwtException("Invalid Google Access Token", ex);
        }
    }
}
