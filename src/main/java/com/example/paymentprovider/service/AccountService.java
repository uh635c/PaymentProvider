package com.example.paymentprovider.service;

import com.example.paymentprovider.entity.AccountEntity;
import com.example.paymentprovider.entity.Currency;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {
//    Flux<AccountEntity> getAllByMerchantId(String id);
    Mono<AccountEntity> getByCurrencyAndMerchantId(Currency currency, String id);
    Mono<AccountEntity> save(AccountEntity account);
    Mono<AccountEntity> getById(String uid);
}
