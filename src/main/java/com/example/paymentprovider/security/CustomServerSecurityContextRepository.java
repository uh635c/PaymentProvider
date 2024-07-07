package com.example.paymentprovider.security;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class CustomServerSecurityContextRepository implements ServerSecurityContextRepository {
    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        MerchantDetails merchantDetails = (MerchantDetails) context.getAuthentication().getPrincipal();
        exchange.getAttributes().put("merchantId", merchantDetails.getMerchantId());
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        return Mono.empty();
    }
}
