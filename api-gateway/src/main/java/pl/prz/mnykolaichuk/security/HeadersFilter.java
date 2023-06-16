package pl.prz.mnykolaichuk.security;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class HeadersFilter extends AbstractGatewayFilterFactory<HeadersFilter.Config> {

    private final ReactiveJwtDecoder jwtDecoder;

    public HeadersFilter(ReactiveJwtDecoder jwtDecoder) {
        super(Config.class);
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Отримання JWT з заголовка Authorization
            String authorizationHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            String jwtToken = authorizationHeader.substring(7); // Видаляємо префікс "Bearer "

            // Розкодування JWT та отримання об'єкта Jwt
            Mono<Jwt> jwtMono = jwtDecoder.decode(jwtToken);

            return jwtMono.flatMap(jwt -> {
                // Отримано об'єкт Jwt
                String userId = jwt.getSubject();
                String email = jwt.getClaim("email");
                // Додавання заголовка user-id до ServerHttpRequest
                ServerHttpRequest request = exchange.getRequest().mutate()
                        .header("email", email)
                        .header("userId", userId)
                        .build();

                // Передача модифікованого ServerHttpRequest до наступного фільтру або маршруту
                return chain.filter(exchange.mutate().request(request).build());
            }).onErrorResume(error -> {
                // Обробка помилки, якщо JWT не може бути розкодований
                return chain.filter(exchange);
            });
        };
    }
    public static class Config {
        // Конфігураційні параметри (якщо потрібно)
    }
}

