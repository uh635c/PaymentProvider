package com.example.paymentprovider.repository;

import com.example.paymentprovider.entity.MerchantEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface MerchantRepository extends R2dbcRepository<MerchantEntity, String> {

    Mono<MerchantEntity> findByName(String name);

}
