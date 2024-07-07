package com.example.paymentprovider.dto;

import com.example.paymentprovider.entity.Currency;
import com.example.paymentprovider.entity.Language;
import com.example.paymentprovider.entity.MethodType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Builder;

import java.net.URI;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TransactionDtoInput (
    String transactionId,
    MethodType paymentMethod,
    Long amount,
    Currency currency,
    CardDto cardData,
    Language language,
    URI notificationUrl,
    CustomerDto customer){
    @Builder(toBuilder = true) public TransactionDtoInput{}
}
