package com.example.paymentprovider.service;

import com.example.paymentprovider.dto.TransactionDto;
import com.example.paymentprovider.dto.TransactionDtoInput;
import com.example.paymentprovider.dto.TransactionDtoOut;
import com.example.paymentprovider.entity.TransactionEntity;
import com.example.paymentprovider.entity.TransactionStatus;
import com.example.paymentprovider.entity.TransactionType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.print.attribute.standard.JobKOctets;
import java.time.LocalDateTime;

public interface TransactionService {
    Mono<TransactionDto> getByUidAndType(String uid, TransactionType type);
    Mono<TransactionDtoOut> createTopup(TransactionDtoInput dtoInput, ServerWebExchange exchange);
    Mono<TransactionDtoOut> createPayout(TransactionDtoInput dtoInput, ServerWebExchange exchange);
    Flux<TransactionDto> getByStartAndEndDateAndType(long start, long end, TransactionType type);
    Mono<Void> notificationProcessor();

}
