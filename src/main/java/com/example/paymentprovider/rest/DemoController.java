package com.example.paymentprovider.rest;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalTime;

@RestController
public class DemoController {

    @GetMapping("/demo")
    public Mono<String> demo(){
        return WebClient.create()
                .get()
                .uri("http://localhost:8081/demo")
                .exchangeToMono(response -> {
                    System.out.println(LocalTime.now());
                    if(!response.statusCode().is2xxSuccessful()){
                        System.out.println("code = "+response.statusCode().value()+"; body = "+response.toEntity(String.class));
                        throw  new RuntimeException();
                    }
                    return response.bodyToMono(String.class);
                })
                .retryWhen(Retry.backoff(5, Duration.ofSeconds(2)));

    }
}
