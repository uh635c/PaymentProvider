package com.example.paymentprovider.dto;

import com.example.paymentprovider.entity.Country;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CustomerDto (
    String firstName,
    String lastName,
    Country country){
    @Builder(toBuilder = true) public CustomerDto{}
}
