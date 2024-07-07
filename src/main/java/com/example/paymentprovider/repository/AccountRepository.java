package com.example.paymentprovider.repository;

import com.example.paymentprovider.entity.AccountEntity;
import com.example.paymentprovider.entity.Currency;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountRepository extends R2dbcRepository<AccountEntity, String> {

    Flux<AccountEntity> findAllByMerchantId(String id);
    Mono<AccountEntity> findByCurrencyAndMerchantId(Currency currency, String merchantId);
}
