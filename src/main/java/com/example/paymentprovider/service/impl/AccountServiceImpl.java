package com.example.paymentprovider.service.impl;

import com.example.paymentprovider.entity.AccountEntity;
import com.example.paymentprovider.entity.Currency;
import com.example.paymentprovider.repository.AccountRepository;
import com.example.paymentprovider.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

//    @Override
//    public Flux<AccountEntity> getAllByMerchantId(String id) {
//        return accountRepository.findAllByMerchantId(id);
//    }

    @Override
    public Mono<AccountEntity> getByCurrencyAndMerchantId(Currency currency, String id) {
        return accountRepository.findByCurrencyAndMerchantId(currency, id);
    }

    @Override
    public Mono<AccountEntity> save(AccountEntity account) {
        return accountRepository.save(account);
    }

    @Override
    public Mono<AccountEntity> getById(String uid) {
        return accountRepository.findById(uid);
    }
}

