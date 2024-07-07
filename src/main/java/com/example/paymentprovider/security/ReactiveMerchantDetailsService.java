package com.example.paymentprovider.security;

import com.example.paymentprovider.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ReactiveMerchantDetailsService implements ReactiveUserDetailsService {

    private final MerchantService merchantService;
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return merchantService.getByName(username).map(MerchantDetails::new);
    }
}
