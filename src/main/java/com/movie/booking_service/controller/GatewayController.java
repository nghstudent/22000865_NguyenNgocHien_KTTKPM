package com.movie.booking_service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.function.Supplier;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/gateway")
public class GatewayController {

    private final RestTemplate restTemplate;

    @Value("${user.service.base-url:http://localhost:8081}")
    private String userServiceBaseUrl;

    @Value("${movie.service.base-url:http://localhost:8082}")
    private String movieServiceBaseUrl;

    public GatewayController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private ResponseEntity<String> executeProxy(Supplier<ResponseEntity<String>> call) {
        try {
            return call.get();
        } catch (HttpStatusCodeException ex) {
            String body = ex.getResponseBodyAsString();
            if (body == null || body.isBlank()) {
                body = "{\"message\":\"" + ex.getStatusText() + "\"}";
            }
            return ResponseEntity.status(ex.getStatusCode()).body(body);
        } catch (ResourceAccessException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("{\"message\":\"Downstream service unavailable\"}");
        }
    }

    @PostMapping("/users/register")
    public ResponseEntity<String> register(@RequestBody Map<String, Object> payload) {
        return executeProxy(() -> {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    userServiceBaseUrl + "/users/register",
                    payload,
                    String.class
            );
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        });
    }

    @PostMapping("/users/login")
    public ResponseEntity<String> login(@RequestBody Map<String, Object> payload) {
        return executeProxy(() -> {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    userServiceBaseUrl + "/users/login",
                    payload,
                    String.class
            );
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        });
    }

    @GetMapping("/movies")
    public ResponseEntity<String> getMovies() {
        return executeProxy(() -> {
            ResponseEntity<String> response = restTemplate.getForEntity(
                    movieServiceBaseUrl + "/movies",
                    String.class
            );
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        });
    }

    @PostMapping("/movies")
    public ResponseEntity<String> addMovie(@RequestBody Map<String, Object> payload) {
        return executeProxy(() -> {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    movieServiceBaseUrl + "/movies",
                    payload,
                    String.class
            );
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        });
    }

    @PutMapping("/movies/{id}")
    public ResponseEntity<String> updateMovie(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        return executeProxy(() -> {
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload);
            ResponseEntity<String> response = restTemplate.exchange(
                    movieServiceBaseUrl + "/movies/" + id,
                    HttpMethod.PUT,
                    request,
                    String.class
            );
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        });
    }
}
