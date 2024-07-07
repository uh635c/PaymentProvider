package com.example.paymentprovider.service.impl;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Base64;

public class GetMerchant {

//    public static String getMerchant(ServerWebExchange exchange){
//
//        String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//        return new String(Base64.getDecoder().decode(header.substring("basic ".length()))).split(":")[0];
//
//    }
}
