package com.example.paymentprovider.service.impl;


import com.example.paymentprovider.entity.MerchantEntity;
import com.example.paymentprovider.repository.MerchantRepository;
import com.example.paymentprovider.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    private final MerchantRepository merchantRepository;

    @Override
    public Mono<MerchantEntity> getByName(String merchantName) {
        return merchantRepository.findByName(merchantName);
    }
}

