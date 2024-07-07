package com.example.paymentprovider.rest;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.postgresql.core.TransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.server.ServerWebExchange;

import com.example.paymentprovider.entity.TransactionEntity;
import com.example.paymentprovider.dto.TransactionDto;
import com.example.paymentprovider.dto.TransactionDtoInput;
import com.example.paymentprovider.dto.TransactionDtoOut;
import com.example.paymentprovider.entity.TransactionType;
import com.example.paymentprovider.exceptions.TransactionFailedException;
import com.example.paymentprovider.service.TransactionService;
import com.example.paymentprovider.util.DataUtils;

import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@ComponentScan("com.example.paymentprovider.errorHandler")
@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = {PayoutController.class})
public class PayoutControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private ServerWebExchange exchange;

    @Test
    @DisplayName("Test get payout by id functionality")
    @WithMockUser
    public void givenId_whenGetPayout_thenTransactionIsReturned() {

        //given
        TransactionDto transactionDto = DataUtils.getTransactionDto();

        BDDMockito.given(transactionService.getByUidAndType(anyString(), any(TransactionType.class)))
                .willReturn(Mono.just(transactionDto));

        //when
        WebTestClient.ResponseSpec result = webTestClient.get()
                .uri("/api/v1/payments/payout/transactionId/details")
                .exchange();

        //then
        result.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.transaction_id").isEqualTo(transactionDto.transactionId())
                .jsonPath("$.status").isEqualTo("COMPLETED")
                .jsonPath("$.message").isEqualTo("OK");


    }

    @Test
    @DisplayName("Test get transaction by incorrect id functionality")
    @WithMockUser
    public void givenIncorrectId_whenGetTransaction_thenExceptionIsReturned() {

        //given
        BDDMockito.given(transactionService.getByUidAndType(anyString(), any(TransactionType.class)))
                .willThrow(new RuntimeException("Transaction with id = transactionId does not exist"));

        //when
        WebTestClient.ResponseSpec result = webTestClient.get()
                .uri("/api/v1/payments/payout/transactionId/details")
                .exchange();

        //then
        result.expectStatus().isNotFound()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$").isEqualTo("Transactions not found");
    }

    @Test
    @DisplayName("Test get transactions by start and end date functionality")
    @WithMockUser
    public void givenStartAndEndDate_whenGetTransactionsList_thenTransactionsAreReturned() {

        //given
        TransactionDto transactionDto1 = DataUtils.getTransactionDto();
        TransactionDto transactionDto2 = DataUtils.getTransactionDto2();

        BDDMockito.given(transactionService.getByStartAndEndDateAndType(anyLong(), anyLong(), any(TransactionType.class)))
                .willReturn(Flux.just(transactionDto1, transactionDto2));

        //when
        WebTestClient.ResponseSpec result = webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/payments/payout/list")
                        .queryParam("start_date", 123)
                        .queryParam("end_date", 456)
                        .build())
                .exchange();

        //then
        result.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.size()").isEqualTo(2);
    }

    @Test
    @DisplayName("Test get transactions by incorrect start or end date functionality")
    @WithMockUser
    public void givenIncorrectStartOrEndDate_whenGetTransactionList_thenExceptionIsReturned() {

        //given

        BDDMockito.given(transactionService.getByStartAndEndDateAndType(anyLong(), anyLong(), any(TransactionType.class)))
                .willThrow(new RuntimeException("There ara no transactions between 123 and 456"));

        //when
        WebTestClient.ResponseSpec result = webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/payments/payout/list")
                        .queryParam("start_date", 123)
                        .queryParam("end_date", 456)
                        .build())
                .exchange();

        //then
        result.expectStatus().isNotFound()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$").isEqualTo("Transactions not found");
    }

    @Test
    @DisplayName("Test create transaction functionality")
    @WithMockUser
    public void givenDtoInput_whenCreate_thentransactionDtoOutIsReturned() {

        //given
        TransactionDtoOut transactionDtoOut = DataUtils.getTransactionDtoOut();
        TransactionDtoInput transactionDtoInput = DataUtils.geTransactionDtoInput();

        BDDMockito.given(transactionService.createPayout(any(TransactionDtoInput.class), any(ServerWebExchange.class))) //TODO ServerWebExchange where from?
                .willReturn(Mono.just(transactionDtoOut));

        //when
        WebTestClient.ResponseSpec result = webTestClient
                .mutateWith(csrf())
                .post()
                .uri("/api/v1/payments/payout")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(transactionDtoInput), TransactionDtoInput.class)
                .exchange();

        //then
        result.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.status").isEqualTo("IN_PROGRESS")
                .jsonPath("$.message").isEqualTo("OK");
    }

    @Test
    @DisplayName("Test create transaction faild functionality")
    @WithMockUser
    public void givenIncorrectDtoInput_whenCreate_thenExceptionIsReturned() {

        //given
        TransactionDtoInput transactionDtoInput = DataUtils.geTransactionDtoInput();

        BDDMockito.given(transactionService.createPayout(any(TransactionDtoInput.class), any(ServerWebExchange.class))) //TODO ServerWebExchange where from?
                .willThrow(new TransactionFailedException("Account is not found", "PAYOUT_FAILED"));

        //when
        WebTestClient.ResponseSpec result = webTestClient
                .mutateWith(csrf())
                .post()
                .uri("/api/v1/payments/payout")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(transactionDtoInput), TransactionDtoInput.class)
                .exchange();

        //then
        result.expectStatus().isBadRequest()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.status").isEqualTo("FAILED")
                .jsonPath("$.message").isEqualTo("PAYOUT_MIN_AMOUNT");
    }
}
