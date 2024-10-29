package org.example.ecommerce.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.example.ecommerce.models.Role;
import org.example.ecommerce.services.AdminService;
import org.example.ecommerce.services.CustomerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;
    private final CustomBasicAuthEntryPoint customBasicAuthEntryPoint;
    private final CustomBearerTokenAuthEntryPoint customBearerTokenAuthEntryPoint;
    private final CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler;
    private final CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler;

    public SecurityConfigurations(CustomBasicAuthEntryPoint customBasicAuthEntryPoint, CustomBearerTokenAuthEntryPoint customBearerTokenAuthEntryPoint, CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler, CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler) throws NoSuchAlgorithmException {
        this.customBasicAuthEntryPoint = customBasicAuthEntryPoint;
        this.customBearerTokenAuthEntryPoint = customBearerTokenAuthEntryPoint;
        this.customBearerTokenAccessDeniedHandler = customBearerTokenAccessDeniedHandler;
        this.customOAuth2LoginSuccessHandler = customOAuth2LoginSuccessHandler;

        // Generate RSA key pair
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // key size in bits
        var keyPair = keyPairGenerator.generateKeyPair();
        this.publicKey = (RSAPublicKey) keyPair.getPublic();
        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests

                        //Login
                                .requestMatchers("/admins/**").hasAuthority(Role.ROLE_ADMIN.name())

                        //Auth
                                .requestMatchers(HttpMethod.POST,
                                        "/login/**").permitAll()
                                .requestMatchers(HttpMethod.GET,
                                        "/login/validate-token").hasAnyAuthority(
                                                Role.ROLE_USER.name(), Role.ROLE_ADMIN.name())


                                //Carts
                                .requestMatchers("/api/v1/carts/**").hasAuthority(Role.ROLE_USER.name())

                        //Categories
                                .requestMatchers(HttpMethod.GET,"/api" +
                                        "/categories/**").permitAll()
                                .requestMatchers(HttpMethod.PUT,"/api" +
                                        "/categories/**").hasAuthority(Role.ROLE_ADMIN.name())
                                .requestMatchers(HttpMethod.POST,"/api" +
                                        "/categories/**").hasAuthority(Role.ROLE_ADMIN.name())
                                .requestMatchers(HttpMethod.DELETE,"/api" +
                                        "/categories/**").hasAuthority(Role.ROLE_ADMIN.name())

                        //Credit Card
                                .requestMatchers("/api/v1/credit-card/**").hasAuthority(Role.ROLE_USER.name())

                        //Customer
                                .requestMatchers("/customers/profile/**").hasAuthority(Role.ROLE_USER.name())
                                .requestMatchers("/customers/checkEmail").permitAll()
                                .requestMatchers(HttpMethod.POST,"/customers/register").permitAll()
                                .requestMatchers(HttpMethod.GET,"/customers/check-info").permitAll()
//                                .requestMatchers(HttpMethod.GET,"/customers").hasAuthority(Role.ROLE_ADMIN.name())

                        //Orders
                                .requestMatchers("/api/v1/orders/admin/**").hasAuthority(Role.ROLE_ADMIN.name())
                                .requestMatchers("/api/v1/orders/customer/**").hasAuthority(Role.ROLE_USER.name())
                                .requestMatchers(HttpMethod.POST,"/api/v1" +
                                        "/orders").hasAuthority(Role.ROLE_USER.name())

                        //Payment
                                .requestMatchers("/api/v1/payment/**").permitAll()

                        //Products
                                .requestMatchers(HttpMethod.GET,"/api" +
                                        "/products/**").permitAll()
                                .requestMatchers(HttpMethod.PUT,"/api" +
                                        "/products/**").hasAuthority(Role.ROLE_ADMIN.name())
                                .requestMatchers(HttpMethod.POST,"/api" +
                                        "/products/**").hasAuthority(Role.ROLE_ADMIN.name())
                                .requestMatchers(HttpMethod.DELETE,"/api" +
                                        "/products/**").hasAuthority(Role.ROLE_ADMIN.name())

                        //Products Specification
                                .requestMatchers(HttpMethod.GET,"/api" +
                                        "/product-specification/**").permitAll()
                                .requestMatchers(HttpMethod.PUT,"/api" +
                                        "/product-specification/**").hasAuthority(Role.ROLE_ADMIN.name())
                                .requestMatchers(HttpMethod.POST,"/api" +
                                        "/product-specification/**").hasAuthority(Role.ROLE_ADMIN.name())
                                .requestMatchers(HttpMethod.DELETE,"/api" +
                                        "/product-specification/**").hasAuthority(Role.ROLE_ADMIN.name())

                        // Sub Categories
                                .requestMatchers(HttpMethod.GET,"/api" +
                                        "/subcategories/**").permitAll()
                                .requestMatchers(HttpMethod.PUT,"/api" +
                                        "/subcategories/**").hasAuthority(Role.ROLE_ADMIN.name())
                                .requestMatchers(HttpMethod.POST,"/api" +
                                        "/subcategories/**").hasAuthority(Role.ROLE_ADMIN.name())
                                .requestMatchers(HttpMethod.DELETE,"/api" +
                                        "/subcategories/**").hasAuthority(Role.ROLE_ADMIN.name())
                )
                .httpBasic(httpBasic -> httpBasic.authenticationEntryPoint(this.customBasicAuthEntryPoint))
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(this.customBearerTokenAuthEntryPoint)
                        .accessDeniedHandler(this.customBearerTokenAccessDeniedHandler)
                )
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth2Login -> oauth2Login
                        .defaultSuccessUrl("/web/index.html", true)
                        .successHandler(customOAuth2LoginSuccessHandler)
                        .failureUrl("/web/auth/login.html?error=true")
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/web/auth/login.html")
                        .defaultSuccessUrl("/web/index.html", true)
                        .failureUrl("/web/auth/login.html?error=true")
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public UserDetailsService userDetailsService(CustomerService customerService, AdminService adminService) {
        return email -> {
            try {
                return customerService.loadUserByUsername(email);
            } catch (UsernameNotFoundException e) {
                return adminService.loadUserByUsername(email);
            }
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(this.privateKey).build();
        JWKSource<SecurityContext> jwkSet = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.publicKey).build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}