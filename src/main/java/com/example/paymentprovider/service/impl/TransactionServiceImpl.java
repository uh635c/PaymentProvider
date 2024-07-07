package com.example.paymentprovider.service.impl;

import com.example.paymentprovider.dto.TransactionDto;
import com.example.paymentprovider.dto.TransactionDtoInput;
import com.example.paymentprovider.dto.TransactionDtoOut;
import com.example.paymentprovider.entity.*;
import com.example.paymentprovider.exceptions.TransactionFailedException;
import com.example.paymentprovider.mappers.CardMapper;
import com.example.paymentprovider.mappers.TransactionMapper;
import com.example.paymentprovider.repository.TransactionRepository;
import com.example.paymentprovider.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionMapper transactionMapper;
    private final AccountService accountService;
    private final TransactionRepository transactionRepository;
    private final CustomerService customerService;
    private final CardService cardService;
    private final NotificationService notificationService;
    private final WebClient webClient;
    private final TransactionalOperator operator;


    @Override
    public Mono<TransactionDto> getByUidAndType(String uid, TransactionType type) {

        return transactionRepository.findByIdAndType(uid, type)
                .switchIfEmpty(Mono.error(new RuntimeException("Transaction with id = " + uid + " does not exist")))
                .flatMap(t -> cardService.findById(t.getCardId())
                        .map(crd -> {
                            t.setCard(crd);
                            return crd;
                        })
                        .flatMap(crd -> customerService.findById(crd.getCustomerId())
                                .map(cstm -> {
                                    t.setCustomer(cstm);
                                    return t;
                                })
                        )
                )
                .map(transactionMapper::mapToDto);
    }


    @Override
    public Flux<TransactionDto> getByStartAndEndDateAndType(long start, long end, TransactionType type) {

        return transactionRepository
                .findAllByCreatedAtBetweenAndType(LocalDateTime.ofEpochSecond(start, 0, ZoneOffset.UTC),
                        LocalDateTime.ofEpochSecond(end, 0, ZoneOffset.UTC),
                        type)
                .switchIfEmpty(Mono.error(new RuntimeException("There ara no transactions between " + start + " and " + end)))
                .flatMap(t -> cardService.findById(t.getCardId())
                        .map(crd -> {
                            t.setCard(crd);
                            return crd;
                        })
                        .flatMap(crd -> customerService.findById(crd.getCustomerId())
                                .map(cstm -> {
                                    t.setCustomer(cstm);
                                    return t;
                                })
                        )
                )
                .map(transactionMapper::mapToDto);
    }


    @Override
    public Mono<TransactionDtoOut> createTopup(TransactionDtoInput dtoInput, ServerWebExchange exchange) {

        Mono<AccountEntity> monoAccount = accountService
                .getByCurrencyAndMerchantId(dtoInput.currency(), (String) exchange.getAttributes().get("merchantId"))
                .switchIfEmpty(Mono.error(new TransactionFailedException("Merchant is not found", "TOPUP_FAILED")));


        Mono<CardEntity> monoCard = cardService.findByCardNumber(dtoInput);

        return Mono.zip(monoAccount, monoCard)
                .flatMap(tuple -> {
                    TransactionEntity transaction = transactionMapper.mapToEntity(dtoInput)
                            .toBuilder()
                            .type(TransactionType.TOPUP)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .status(TransactionStatus.IN_PROGRESS)
                            .cardId(tuple.getT2().getId())
                            .accountId(tuple.getT1().getId())
                            .build();
                    return transactionRepository.save(transaction)
                            .map(transactionMapper::mapToDtoOut);
                });


    }

    @Override
    public Mono<TransactionDtoOut> createPayout(TransactionDtoInput dtoInput, ServerWebExchange exchange) {

        Mono<AccountEntity> monoAccount = accountService
                .getByCurrencyAndMerchantId(dtoInput.currency(), exchange.getAttribute("merchantId"))
                .switchIfEmpty(Mono.error(new TransactionFailedException("Merchant is not found", "PAYOUT_FAILED")))
                .flatMap(account -> {
                    long balance = account.getBalance() - dtoInput.amount();
                    if (balance < 0) {
                        return Mono.error(new TransactionFailedException("Not enough money on the balance", "PAYOUT_FAILED"));
                    }
                    account.setBalance(balance);
                    return accountService.save(account);
                });

        Mono<CardEntity> monoCard = cardService
                .findByCardNumber(dtoInput.cardData().cardNumber())
                .switchIfEmpty(Mono.error(new TransactionFailedException("Card is not found", "PAYOUT_FAILED")));

        return Mono.zip(monoCard, monoAccount)
                .flatMap(tuple -> {
                    TransactionEntity transaction = transactionMapper.mapToEntity(dtoInput)
                            .toBuilder()
                            .type(TransactionType.PAYOUT)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .status(TransactionStatus.IN_PROGRESS)
                            .cardId(tuple.getT1().getId())
                            .accountId(tuple.getT2().getId())
                            .build();
                    return transactionRepository.save(transaction)
                            .map(transactionMapper::mapToDtoOut);
                });
    }


    @Override
    public Mono<Void> notificationProcessor() {

        return transactionRepository.findAllByStatus(TransactionStatus.IN_PROGRESS)
                .flatMap(t -> cardService.findById(t.getCardId())
                        .map(crd -> {
                            t.setCard(crd);
                            return crd;
                        })
                        .flatMap(crd -> customerService.findById(crd.getCustomerId())
                                .map(cstm -> {
                                    t.setCustomer(cstm);
                                    return t;
                                })
                        )
                )
                .flatMap(transaction -> {
                    transaction.setUpdatedAt(LocalDateTime.now());
                    transaction.setStatus(getTransactionStatus());

                    if (transaction.getType().equals(TransactionType.TOPUP) &&
                            transaction.getStatus().equals(TransactionStatus.COMPLETED) ||
                            transaction.getType().equals(TransactionType.PAYOUT) &&
                                    transaction.getStatus().equals(TransactionStatus.FAILED)) {

                        return accountService.getById(transaction.getAccountId())
                                .flatMap(account -> {
                                    account.setBalance(account.getBalance() + transaction.getAmount());
                                    return accountService.save(account);
                                })
                                .then(transactionRepository.save(transaction))
                                .as(operator::transactional);
                    }

                    return transactionRepository.save(transaction)
                            .as(operator::transactional);
                })
                .flatMap(transaction -> webClient
                        .post()
                        .uri(transaction.getUrl()/*"http://localhost:8081/demo"*/)
                        .body(Mono.just(transactionMapper.mapToNotificationDto(transaction)), TransactionEntity.class)
                        .exchangeToMono(response -> response.bodyToMono(String.class)
                                .flatMap(body -> notificationService.save(NotificationEntity.builder()
                                                .attempt(1)
                                                .createdAt(LocalDateTime.now())
                                                .transactionId(transaction.getId())
                                                .url(transaction.getUrl())
                                                .message("OK")
                                                .responseBody(body)
                                                .responseCode(response.statusCode().value())
                                                .build())
                                        .as(operator::transactional))
                                .flatMap(n -> {
                                    if (!response.statusCode().is2xxSuccessful()) {
                                        return Mono.error(new RuntimeException());
                                    }
                                    return Mono.empty();
                                }))
                        .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))))
                .then();
    }

    //////////////////////////////////////////// Private Methods ///////////////////////////////////////////////////////


    private TransactionStatus getTransactionStatus() {
        Random random = new Random();
        if (random.nextInt(10) < 9) {
            return TransactionStatus.COMPLETED;
        }
        return TransactionStatus.FAILED;
    }
}
