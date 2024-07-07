package com.example.paymentprovider.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Builder;

import lombok.Data;
import org.mapstruct.Mapping;

import java.sql.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CardDto (
    Long cardNumber,
    String expDate,
    Integer cvv){
    @Builder(toBuilder = true) public CardDto{}
}
