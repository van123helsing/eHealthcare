package com.example.apigateway.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class AuthTokenFilter extends AbstractGatewayFilterFactory<AuthTokenFilter.Config> {

    @Autowired
    WebClient.Builder webClientBuilder;

    public AuthTokenFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onFailure(exchange);
            }

            String authToken = Objects.requireNonNull(request.getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);

            return webClientBuilder.build().get()
                    .uri("lb://auth-service/auth")
                    .header(HttpHeaders.AUTHORIZATION, authToken)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(ConnValidationResponse.class)
                    .map(response -> {
                        exchange.getRequest().mutate().header("username", response.getUsername());
                        exchange.getRequest().mutate().header("authorities", response.getAuthorities().stream()
                                .map(Authorities::getAuthority).reduce("", (a, b) -> a + "," + b));

                        return exchange;
                    }).flatMap(chain::filter).onErrorResume(error -> onFailure(exchange));
        });
    }

    private Mono<Void> onFailure(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    public static class Config {

    }

}
