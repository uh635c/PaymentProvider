package com.example.paymentprovider.dto;

import com.example.paymentprovider.entity.Currency;
import com.example.paymentprovider.entity.Language;
import com.example.paymentprovider.entity.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

import java.net.URI;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TransactionDto (
    String transactionId,
    String paymentMethod,
    Long amount,
    Currency currency,
    CardDtoNumber cardData,
    Language language,
    URI notificationUrl,
    CustomerDto customer,
    LocalDateTime createdAt,
    LocalDateTime updateAt,
    TransactionStatus status,
    String message){

    @Builder(toBuilder = true) public TransactionDto{}
}
