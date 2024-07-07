package com.example.paymentprovider.service;

import com.example.paymentprovider.dto.TransactionDtoInput;
import com.example.paymentprovider.entity.Country;
import com.example.paymentprovider.entity.CustomerEntity;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Mono<CustomerEntity> findById(String id);
    Mono<CustomerEntity> findByFirstNameAndLastNameAndCountry(TransactionDtoInput dtoInput);
}
