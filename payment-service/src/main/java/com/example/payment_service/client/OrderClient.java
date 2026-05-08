package com.example.payment_service.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Component
public class OrderClient {

    private final RestTemplate restTemplate;
    private final String orderServiceUrl;

    public OrderClient(
            RestTemplate restTemplate,
            @Value("${client.order-service.url}") String orderServiceUrl) {
        this.restTemplate = restTemplate;
        this.orderServiceUrl = orderServiceUrl;
    }

    @CircuitBreaker(name = "orderClient", fallbackMethod = "markPaidFallback")
    @Retry(name = "orderClient")
    @RateLimiter(name = "orderClient")
    @TimeLimiter(name = "orderClient")
    public CompletableFuture<Boolean> markPaid(Long orderId) {
        return CompletableFuture.supplyAsync(() -> {
            restTemplate.postForObject(orderServiceUrl + "/orders/" + orderId + "/paid", null, Object.class);
            return true;
        });
    }

    private CompletableFuture<Boolean> markPaidFallback(Long orderId, Throwable throwable) {
        return CompletableFuture.completedFuture(false);
    }
}
