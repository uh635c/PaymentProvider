package com.example.paymentprovider.rest;

import com.example.paymentprovider.dto.TransactionDto;
import com.example.paymentprovider.dto.TransactionDtoInput;
import com.example.paymentprovider.dto.TransactionDtoOut;
import com.example.paymentprovider.entity.TransactionType;
import com.example.paymentprovider.service.TransactionService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RestController
@RequestMapping(path = "/api/v1/payments/transaction")
@RequiredArgsConstructor
public class TopupController {

    private final TransactionService transactionService;

    @GetMapping("/{uid}/details")
    public Mono<TransactionDto> getTransaction(@PathVariable String uid) {

        return transactionService.getByUidAndType(uid, TransactionType.TOPUP);
    }

    @GetMapping("/list")
    public Flux<TransactionDto> getTransactionList(@RequestParam("start_date") long start,
                                                   @RequestParam("end_date") long end) {

        return transactionService.getByStartAndEndDateAndType(start, end, TransactionType.TOPUP);
    }

    @PostMapping()
    public Mono<TransactionDtoOut> create(@RequestBody TransactionDtoInput dtoInput, ServerWebExchange exchange) {

        return transactionService.createTopup(dtoInput, exchange);
    }

}
