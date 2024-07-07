package com.example.paymentprovider.service;

import com.example.paymentprovider.dto.TransactionDtoInput;
import com.example.paymentprovider.entity.CardEntity;
import reactor.core.publisher.Mono;

public interface CardService {
    Mono<CardEntity> findById(String id);
    Mono<CardEntity> findByCardNumber(TransactionDtoInput dtoInput);
    Mono<CardEntity> findByCardNumber(Long cardNumber);
}
