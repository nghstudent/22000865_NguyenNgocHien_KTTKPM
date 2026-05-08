package com.example.order_service.client;

import com.example.order_service.model.Food;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Component
public class DownstreamClient {

    private final RestTemplate restTemplate;
    private final String userServiceUrl;
    private final String foodServiceUrl;

    public DownstreamClient(
            RestTemplate restTemplate,
            @Value("${client.user-service.url}") String userServiceUrl,
            @Value("${client.food-service.url}") String foodServiceUrl) {
        this.restTemplate = restTemplate;
        this.userServiceUrl = userServiceUrl;
        this.foodServiceUrl = foodServiceUrl;
    }

    @CircuitBreaker(name = "userClient", fallbackMethod = "usersFallback")
    @Retry(name = "userClient")
    @RateLimiter(name = "userClient")
    @TimeLimiter(name = "userClient")
    public CompletableFuture<Object[]> getUsers() {
        return CompletableFuture.supplyAsync(() ->
                restTemplate.getForObject(userServiceUrl + "/users", Object[].class));
    }

    @CircuitBreaker(name = "foodClient", fallbackMethod = "foodsFallback")
    @Retry(name = "foodClient")
    @RateLimiter(name = "foodClient")
    @TimeLimiter(name = "foodClient")
    public CompletableFuture<Food[]> getFoods() {
        return CompletableFuture.supplyAsync(() ->
                restTemplate.getForObject(foodServiceUrl + "/foods", Food[].class));
    }

    private CompletableFuture<Object[]> usersFallback(Throwable throwable) {
        return CompletableFuture.completedFuture(new Object[0]);
    }

    private CompletableFuture<Food[]> foodsFallback(Throwable throwable) {
        return CompletableFuture.completedFuture(new Food[0]);
    }
}
