package com.example.paymentprovider.repository;

import com.example.paymentprovider.entity.TransactionEntity;
import com.example.paymentprovider.entity.TransactionStatus;
import com.example.paymentprovider.entity.TransactionType;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends R2dbcRepository<TransactionEntity, String> {

    Mono<TransactionEntity> findByIdAndType (String uid, TransactionType type);
    
    Flux<TransactionEntity> findAllByCreatedAtBetweenAndType (LocalDateTime start, LocalDateTime end, TransactionType type);

    Flux<TransactionEntity> findAllByStatus(TransactionStatus status);

}
