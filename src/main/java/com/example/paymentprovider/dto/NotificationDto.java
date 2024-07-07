package com.example.paymentprovider.dto;

import com.example.paymentprovider.entity.Currency;
import com.example.paymentprovider.entity.Language;
import com.example.paymentprovider.entity.TransactionStatus;
import com.example.paymentprovider.entity.TransactionType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record NotificationDto(
        String transactionId,
        String paymentMethod,
        Long amount,
        Currency currency,
        TransactionType type,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        CardDtoNumber cardData,
        Language language,
        CustomerDto customer,
        TransactionStatus status,
        String message) {}
