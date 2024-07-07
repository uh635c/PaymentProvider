package com.example.paymentprovider.repository;

import com.example.paymentprovider.entity.CardEntity;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcConnectionDetails;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface CardRepository extends R2dbcRepository<CardEntity, String> {

    Mono<CardEntity> findByCardNumber(Long cardNumber);
}
