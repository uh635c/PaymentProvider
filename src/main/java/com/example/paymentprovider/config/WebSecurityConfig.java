package com.example.paymentprovider.config;


import com.example.paymentprovider.repository.MerchantRepository;
import com.example.paymentprovider.security.CustomServerSecurityContextRepository;
import com.example.paymentprovider.security.ReactiveMerchantDetailsService;
import com.example.paymentprovider.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final MerchantService merchantService;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){

        return http
                .csrf((csrf) -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.securityContextRepository(new CustomServerSecurityContextRepository()))
                .authorizeExchange(auth -> auth.anyExchange().authenticated())
                .build();
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService(){
        return new ReactiveMerchantDetailsService(merchantService);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public WebClient webClient(){
        return WebClient.create();
    }
}
