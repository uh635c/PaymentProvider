package com.example.paymentprovider.mappers;


import com.example.paymentprovider.dto.CardDto;
import com.example.paymentprovider.dto.CardDtoNumber;
import com.example.paymentprovider.entity.CardEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface CardMapper {


    CardDto mapToDto(CardEntity entity);

    @Mapping(target = "expirationDate", expression = "java(creditCardExpire(dto))")
    CardEntity mapToEntity(CardDto dto);

    CardDtoNumber mapToCardNumber(CardEntity entity);

    default LocalDate creditCardExpire(CardDto dto) {
        String[] strings = dto.expDate().split("/");
        int year = Integer.parseInt(strings[1]);
        int month = Integer.parseInt(strings[0]);

        return  LocalDate.of(2000+year, month, 1);
    }

}
