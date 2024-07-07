package com.example.paymentprovider.util;

import com.example.paymentprovider.dto.*;
import com.example.paymentprovider.entity.*;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DataUtils {



    public static AccountEntity getAccount1ForMerchant1Persistent() {
        return  AccountEntity.builder()
                .id("accountId1")
                .currency(Currency.EUR)
                .balance(1000L)
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .merchantId("merchant1")
                .build();
    }

    public static AccountEntity getAccount2ForMerchant1Persistent() {
        return  AccountEntity.builder()
                .id("accountId2")
                .currency(Currency.EUR)
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .merchantId("merchant1")
                .build();
    }

    public static AccountEntity getAccount3ForMerchant1Persistent() {
        return  AccountEntity.builder()
                .id("accountId3")
                .currency(Currency.EUR)
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .merchantId("merchant1")
                .build();
    }


    public static TransactionEntity getTransactionPersistant() {
        TransactionEntity transaction = null;
        try {
             transaction = TransactionEntity.builder()
                    .id("transactionId")
                    .type(TransactionType.TOPUP)
                    .paymentMethod(MethodType.CARD)
                    .amount(100l)
                    .language(Language.ENGLISH)
                    .url(new URI("v1/test"))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .status(TransactionStatus.IN_PROGRESS)
                    .cardId("cardId")
                    .accountId("accountId")
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return transaction;
    }

    public static TransactionEntity getTransaction2Persistant() {
        TransactionEntity transaction = null;
        try {
            transaction = TransactionEntity.builder()
                    .id("transactionId2")
                    .type(TransactionType.PAYOUT)
                    .paymentMethod(MethodType.CARD)
                    .amount(100l)
                    .language(Language.ENGLISH)
                    .url(new URI("https://mail.com"))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .status(TransactionStatus.COMPLETED)
                    .cardId("cardId2")
                    .accountId("accountId2")
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return transaction;
    }

    public static CardEntity getCardPersistent(){
        return CardEntity.builder()
                .id("cardId")
                .cardNumber(123456789L)
                .currency(Currency.EUR)
                .cvv(123L)
                .expirationDate(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .customerId("customerId")
                .build();
    }

    public static CardEntity getCard2Persistent(){
        return CardEntity.builder()
                .id("cardId2")
                .cardNumber(987654321l)
                .currency(Currency.EUR)
                .cvv(123L)
                .expirationDate(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .customerId("customerId2")
                .build();
    }


    public static CustomerEntity getCustomerPersistent(){
        return CustomerEntity.builder()
                .id("customerId")
                .firstName("John")
                .lastName("Doe")
                .country(Country.RUSSIA)
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    public static CustomerEntity getCustomer2Persistent(){
        return CustomerEntity.builder()
                .id("customerId2")
                .firstName("Mike")
                .lastName("Tyson")
                .country(Country.RUSSIA)
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    public static TransactionDtoInput geTransactionDtoInput(){
        TransactionDtoInput transaction = null;
        try {
            transaction = TransactionDtoInput.builder()
            .paymentMethod(MethodType.CARD)
            .amount(100L)
            .currency(Currency.EUR)
            .cardData(CardDto.builder()
                .cardNumber(123456789L)
                .expDate("5/24")
                .cvv(123)
                .build())
            .language(Language.ENGLISH)
            .notificationUrl(new URI("https://mail.com"))
            .customer(CustomerDto.builder()
                .firstName("John")
                .lastName("Doe")
                .country(Country.RUSSIA)
                .build())
            .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return transaction;
    }

    public static TransactionDtoInput getPayoutDtoInput(){
        TransactionDtoInput transaction = null;
        try {
            transaction = TransactionDtoInput.builder()
                    .paymentMethod(MethodType.CARD)
                    .amount(100L)
                    .currency(Currency.EUR)
                    .cardData(CardDto.builder()
                            .cardNumber(123456789L)
                            .build())
                    .language(Language.ENGLISH)
                    .notificationUrl(new URI("https://mail.com"))
                    .customer(CustomerDto.builder()
                            .firstName("John")
                            .lastName("Doe")
                            .country(Country.RUSSIA)
                            .build())
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return transaction;
    }

    public static TransactionDto getTransactionDto(){
        try {
            return TransactionDto.builder()
                    .transactionId("transactionDtoId")
                    .paymentMethod("CARD")
                    .amount(100L)
                    .currency(Currency.EUR)
                    .cardData(new CardDtoNumber(123456789L))
                    .language(Language.ENGLISH)
                    .notificationUrl(new URI("https://mail.com"))
                    .customer(CustomerDto.builder()
                            .firstName("Jhon")
                            .lastName("Doe")
                            .country(Country.RUSSIA)
                            .build())
                    .createdAt(LocalDateTime.now())
                    .updateAt(LocalDateTime.now())
                    .status(TransactionStatus.COMPLETED)
                    .message("OK")
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static TransactionDto getTransactionDto2(){
        try {
            return TransactionDto.builder()
                    .transactionId("transactionDtoId2")
                    .paymentMethod("CARD")
                    .amount(1000L)
                    .currency(Currency.EUR)
                    .cardData(new CardDtoNumber(123456789L))
                    .language(Language.ENGLISH)
                    .notificationUrl(new URI("https://mail.com"))
                    .customer(CustomerDto.builder()
                            .firstName("Jhon")
                            .lastName("Doe")
                            .country(Country.RUSSIA)
                            .build())
                    .createdAt(LocalDateTime.now())
                    .updateAt(LocalDateTime.now())
                    .status(TransactionStatus.COMPLETED)
                    .message("OK")
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static NotificationEntity getNotificationEntityPersistentFail(){
        NotificationEntity notification = null;
        try {
            notification = NotificationEntity.builder()
                    .attempt(1)
                    .createdAt(LocalDateTime.now())
                    .transactionId("transactionId")
                    .url(new URI("mail.com"))
                    .message("OK")
                    .responseBody("body")
                    .responseCode(HttpStatus.BAD_REQUEST.value())
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return notification;
    }

    public static TransactionEntity getTransactionPersistantForNotification(String url) {
        TransactionEntity transaction = null;
        try {
            transaction = TransactionEntity.builder()
                    .id("transactionId")
                    .type(TransactionType.TOPUP)
                    .paymentMethod(MethodType.CARD)
                    .amount(100l)
                    .language(Language.ENGLISH)
                    .url(new URI(url))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .status(TransactionStatus.IN_PROGRESS)
                    .cardId("cardId")
                    .accountId("accountId")
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return transaction;
    }

    public static TransactionDtoOut getTransactionDtoOut(){
        return TransactionDtoOut.builder()
                .transactionId("transactionDtoOutId")
                .status(TransactionStatus.IN_PROGRESS)
                .message("OK")
                .build();
    }

}
