package com.example.paymentprovider.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import reactor.core.publisher.Mono;

//import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class CronService {

    private final TransactionService transactionService;

//    @Scheduled(fixedDelay = 600, timeUnit = TimeUnit.SECONDS)
//    public Mono<Void> fakeTransactionProcessing() {
//       return transactionService.notificationProcessor();
//    }

}
