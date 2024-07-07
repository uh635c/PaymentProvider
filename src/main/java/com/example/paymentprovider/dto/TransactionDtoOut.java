package com.example.paymentprovider.dto;

import com.example.paymentprovider.entity.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TransactionDtoOut (
        String transactionId,
        TransactionStatus status,
        String message){
    @Builder(toBuilder = true) public TransactionDtoOut{};
}
