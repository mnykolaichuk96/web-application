package pl.prz.mnykolaichuk.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


@RequiredArgsConstructor
@Configuration
@EnableWebFluxSecurity      // Because Security is from Spring Web Flux project
public class WebSecurityConfig {
    public static final String ADMIN = "admin";
    public static final String USER = "user";
    private final JwtAuthConverter jwtAuthConverter;
    // Конфігурація безпеки за допомогою WebFlux
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity
                .authorizeExchange()        // початок конфігурації обміну авторизацією.
                // Дозволяємо прямий доступ до API реєстрації
                .pathMatchers( "/api/anonymous/**").permitAll()
                .pathMatchers("/api/admin", "/api/admin/**").hasRole(ADMIN)
                .pathMatchers("/api/order", "/api/order**").hasAnyRole(ADMIN, USER)
                .anyExchange().authenticated()
                .and()
                .csrf().disable()       // відключаємо захист від CSRF-атак. Because we test from postman, to have access
                .oauth2Login()          //активуємо підтримку авторизації з використанням OAuth2.
                .and()
                .oauth2ResourceServer()
                .jwt() //налаштовуємо використання JWT (JSON Web Token) в якості ресурсного сервера OAuth2.
                .jwtAuthenticationConverter(jwtAuthConverter);

        return serverHttpSecurity
                .build();
    }

}
