package com.example.paymentprovider.mappers;

import com.example.paymentprovider.dto.*;
import com.example.paymentprovider.entity.CardEntity;
import com.example.paymentprovider.entity.TransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "message", constant = "OK")
    @Mapping(source = "id", target = "transactionId")
    TransactionDtoOut mapToDtoOut(TransactionEntity entity);

    @Mapping(source = "id", target = "transactionId")
    @Mapping(source = "url", target = "notificationUrl")
    @Mapping(target = "message", constant = "OK")
    @Mapping(target = "currency", source = "entity.card.currency")
    @Mapping(target = "cardData", source = "card")
    TransactionDto mapToDto(TransactionEntity entity);

    CardDtoNumber mapToCardNumber(CardEntity entity);

    @Mapping(source = "notificationUrl", target = "url")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "status", expression = "java(TransactionStatus.IN_PROGRESS)")
    TransactionEntity mapToEntity(TransactionDtoInput dto);

    @Mapping(source = "id", target = "transactionId")
    @Mapping(target = "message", constant = "OK")
    @Mapping(target = "currency", source = "entity.card.currency")
    @Mapping(target = "cardData", source = "card")
    NotificationDto mapToNotificationDto(TransactionEntity entity);


}
