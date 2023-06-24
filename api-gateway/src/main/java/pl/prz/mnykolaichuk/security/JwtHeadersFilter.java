package pl.prz.mnykolaichuk.security;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class JwtHeadersFilter extends AbstractGatewayFilterFactory<JwtHeadersFilter.Config> {

    public JwtHeadersFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Отримання JWT з заголовка Authorization
            String authorizationHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            assert authorizationHeader != null;
            String jwtToken = authorizationHeader.substring(7); // Видаляємо префікс "Bearer "

            // Додавання JWT як заголовка до ServerHttpRequest
            ServerHttpRequest request = exchange.getRequest().mutate()
                    .header("Authorization", "Bearer " + jwtToken)
                    .build();

            // Передача модифікованого ServerHttpRequest до наступного фільтру або маршруту
            return chain.filter(exchange.mutate().request(request).build());
        };
    }

    public static class Config {
        // Конфігураційні параметри (якщо потрібно)
    }
}

