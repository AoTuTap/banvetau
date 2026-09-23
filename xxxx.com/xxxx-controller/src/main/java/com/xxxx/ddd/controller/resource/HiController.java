package com.xxxx.ddd.controller.resource;

import com.xxxx.ddd.application.service.event.EventAppService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.util.Random;

@RestController
@RequestMapping("/hello")
public class HiController {
    @Autowired
    private EventAppService eventAppService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/hi")
    @RateLimiter(name = "backendA", fallbackMethod = "fallbackHello")
    public String hello() {
        return eventAppService.sayHi("Hi");
    }

    public String fallbackHello(RequestNotPermitted ex) {
        return "Too many requests";
    }

    @GetMapping("/hi/v1")
    @RateLimiter(name = "backendB", fallbackMethod = "fallbackHello")
    public String sayHi() {
        return eventAppService.sayHi("Ho");
    }

    private static final SecureRandom secureRandom = new SecureRandom();
    @GetMapping("/circuit/breaker")
    @CircuitBreaker(name = "checkRandom", fallbackMethod = "fallbackCircuitBreaker")
    public String circuitBreaker() {
        int productId = secureRandom.nextInt(20) + 1;
        String url = "https://fakestoreapi.com/products/" + productId;
        System.out.println("Requesting URL: " + url);
        return restTemplate.getForObject(url, String.class);
    }

    public String fallbackCircuitBreaker(Throwable ex) {
        return ex.getMessage();
    }
}
