package com.example.apigateway.config;

import com.example.apigateway.filters.AuthTokenFilter;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GatewayConfig {
    private static final String authServiceUrl = "lb://auth-service";
    private static final String schedulerServiceUrl = "lb://scheduler-service";
    private static final String appointmentsServiceUrl = "lb://appointments-service";
    private static final String surveysServiceUrl = "lb://surveys-service";

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }
    
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder, AuthTokenFilter authFilter) {
        return builder.routes()
            .route("auth-test",  r -> r.path("/auth")
                    .and().method(HttpMethod.GET)
                    .filters(f -> f.filter(authFilter.apply(new AuthTokenFilter.Config())))
                    .uri(authServiceUrl))
            .route("auth",  r -> r.path("/auth-service/auth")
                    .and().method(HttpMethod.GET)
                    .uri(authServiceUrl))
            .route("login", r -> r.path("/login")
                    .and().method(HttpMethod.POST)
                    .uri(authServiceUrl))
            .route("schedules", r -> r.path("/api/scheduler/**")
                    .filters(f -> f.filter(authFilter.apply(new AuthTokenFilter.Config())))
                    .uri(schedulerServiceUrl))
            .route("appointments", r -> r.path("/api/appointments/**")
                    .filters(f -> f.filter(authFilter.apply(new AuthTokenFilter.Config())))
                    .uri(appointmentsServiceUrl))
            .route("surveys", r -> r.path("/api/surveys/**")
                    .filters(f -> f.filter(authFilter.apply(new AuthTokenFilter.Config())))
                    .uri(surveysServiceUrl))
            .build();
    }
}
