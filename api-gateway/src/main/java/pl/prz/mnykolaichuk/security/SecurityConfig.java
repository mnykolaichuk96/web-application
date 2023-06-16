package pl.prz.mnykolaichuk.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

@Configuration
public class SecurityConfig {

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        // Налаштування ReactiveJwtDecoder з використанням NimbusReactiveJwtDecoder
        String issuerUri = "http://localhost:8181/auth/realms/web-application-realm";
        return NimbusReactiveJwtDecoder.withJwkSetUri(issuerUri + "/protocol/openid-connect/certs")
                .build();
    }

    // Інші налаштування безпеки

}

