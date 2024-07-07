package com.example.paymentprovider.service;

import com.example.paymentprovider.entity.MerchantEntity;
import reactor.core.publisher.Mono;

public interface MerchantService {

    Mono<MerchantEntity> getByName(String merchantName);
}
