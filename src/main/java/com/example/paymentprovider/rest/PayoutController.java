package com.example.paymentprovider.rest;

import com.example.paymentprovider.dto.TransactionDto;
import com.example.paymentprovider.dto.TransactionDtoInput;
import com.example.paymentprovider.dto.TransactionDtoOut;
import com.example.paymentprovider.entity.TransactionType;
import com.example.paymentprovider.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/payments/payout")
@RequiredArgsConstructor
public class PayoutController {

    private final TransactionService transactionService;


    @GetMapping("/{uid}/details")
    public Mono<TransactionDto> getById(@PathVariable String uid){

        return transactionService.getByUidAndType(uid, TransactionType.PAYOUT);
    }


    @GetMapping("/list")
    public Flux<TransactionDto> getTransactionList(@RequestParam("start_date") long start,
                                                   @RequestParam("end_date") long end){

        return transactionService.getByStartAndEndDateAndType(start, end, TransactionType.PAYOUT);
    }

    @PostMapping()
    public Mono<TransactionDtoOut> create(@RequestBody TransactionDtoInput dtoInput, ServerWebExchange exchange){


        return transactionService.createPayout(dtoInput, exchange);
    }



}
