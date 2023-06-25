package pl.prz.mnykolaichuk.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

@Configuration
public class SecurityConfig {
    @Autowired
    private KeycloakProperties keycloakProperties;
    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        // Налаштування ReactiveJwtDecoder з використанням NimbusReactiveJwtDecoder
        String issuerUri = keycloakProperties.getIssuerUri();
        return NimbusReactiveJwtDecoder.withJwkSetUri(issuerUri + "/protocol/openid-connect/certs")
                .build();
    }

    // Інші налаштування безпеки

}

