package com.example.paymentprovider.it;

import com.example.paymentprovider.config.PostgreTestContainerConfig;
import com.example.paymentprovider.dto.TransactionDtoInput;
import com.example.paymentprovider.entity.*;
import com.example.paymentprovider.repository.*;
import com.example.paymentprovider.util.DataUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(PostgreTestContainerConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class ItTopupControllerTests {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    WebTestClient webTestClient;

    private MerchantEntity merchantEntity;

    @BeforeEach
    public void setUp() {
        transactionRepository.deleteAll().block();
        cardRepository.deleteAll().block();
        customerRepository.deleteAll().block();
        accountRepository.deleteAll().block();
        merchantRepository.deleteAll().block();
        notificationRepository.deleteAll().block();

        merchantEntity = merchantRepository.save(MerchantEntity.builder()
                .name("merchantName")
                .secretKey("$2a$10$0APXRHr/McLp7F5qqxixTu/JhXkOKjTZt0eipA/bc/Zb7AgWaPyFO")
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build()).block();
    }

    @Test
    @DisplayName("Test get transaction by id functionality")
    public void givenId_whenGetTransaction_thenTransactionIsReturned() {

        //given
        AccountEntity savedAccount = accountRepository
                .save(DataUtils.getAccount1ForMerchant1Persistent()
                        .toBuilder()
                        .id(null)
                        .merchantId(merchantEntity.getId())
                        .build())
                .block();

        CustomerEntity savedCustomer = customerRepository
                .save(DataUtils.getCustomerPersistent()
                        .toBuilder()
                        .id(null)
                        .build())
                .block();

        CardEntity savedCard = cardRepository
                .save(DataUtils.getCardPersistent()
                        .toBuilder()
                        .id(null)
                        .customerId(savedCustomer.getId())
                        .build())
                .block();

        TransactionEntity savedTransaction = transactionRepository
                .save(DataUtils.getTransactionPersistant()
                        .toBuilder()
                        .id(null)
                        .cardId(savedCard.getId())
                        .accountId(savedAccount.getId())
                        .build())
                .block();

        //when
        WebTestClient.ResponseSpec result = webTestClient.get()
                .uri("/api/v1/payments/transaction/" + savedTransaction.getId() + "/details")
                .header("Authorization", "Basic bWVyY2hhbnROYW1lOnBhc3M=")
                .exchange();

        //then
        result.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.transaction_id").isEqualTo(savedTransaction.getId())
                .jsonPath("$.status").isEqualTo("IN_PROGRESS")
                .jsonPath("$.card_data.card_number").isEqualTo(savedCard.getCardNumber())
                .jsonPath("$.message").isEqualTo("OK");
    }

    @Test
    @DisplayName("Test get transaction by incorrect id functionality")
    public void givenIncorrectId_whenGetTransaction_thenExceptionIsReturned() {

        //given

        //when
        WebTestClient.ResponseSpec result = webTestClient.get()
                .uri("/api/v1/payments/transaction/transactionId/details")
                .header("Authorization", "Basic bWVyY2hhbnROYW1lOnBhc3M=")
                .exchange();

        //then
        result.expectStatus().isNotFound()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$").isEqualTo("Transactions not found");

    }

    @Test
    @DisplayName("Test get transactions by start and end date functionality")
    public void givenStartAndEndDate_whenGetTransactionsList_thenTransactionsAreReturned() {
        //given
        AccountEntity savedAccount = accountRepository
                .save(DataUtils.getAccount1ForMerchant1Persistent()
                        .toBuilder()
                        .id(null)
                        .merchantId(merchantEntity.getId())
                        .build())
                .block();

        CustomerEntity savedCustomer = customerRepository
                .save(DataUtils.getCustomerPersistent()
                        .toBuilder()
                        .id(null)
                        .build())
                .block();

        CardEntity savedCard = cardRepository
                .save(DataUtils.getCardPersistent()
                        .toBuilder()
                        .id(null)
                        .customerId(savedCustomer.getId())
                        .build())
                .block();

        TransactionEntity savedTransaction = transactionRepository
                .save(DataUtils.getTransactionPersistant()
                        .toBuilder()
                        .id(null)
                        .cardId(savedCard.getId())
                        .accountId(savedAccount.getId())
                        .build())
                .block();

        //when
        WebTestClient.ResponseSpec result = webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/payments/transaction/list")
                        .queryParam("start_date", 123)
                        .queryParam("end_date", 1730126393)
                        .build())
                .header("Authorization", "Basic bWVyY2hhbnROYW1lOnBhc3M=")
                .exchange();

        //then
        result.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.size()").isEqualTo(1);
    }


    @Test
    @DisplayName("Test get transactions by incorrect start or end date functionality")
    public void givenIncorrectStartOrEndDate_whenGetTransactionList_thenExceptionIsReturned() {

        //given

        //when
        WebTestClient.ResponseSpec result = webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/payments/transaction/list")
                        .queryParam("start_date", 123)
                        .queryParam("end_date", 456)
                        .build())
                .header("Authorization", "Basic bWVyY2hhbnROYW1lOnBhc3M=")
                .exchange();

        //then
        result.expectStatus().isNotFound()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$").isEqualTo("Transactions not found");
    }

    @Test
    @DisplayName("Test create transaction functionality")
    public void givenDtoInput_whenCreate_thentransactionDtoOutIsReturned() {

        //given
        AccountEntity savedAccount = accountRepository
                .save(DataUtils.getAccount1ForMerchant1Persistent()
                        .toBuilder()
                        .id(null)
                        .merchantId(merchantEntity.getId())
                        .build())
                .block();

        CustomerEntity savedCustomer = customerRepository
                .save(DataUtils.getCustomerPersistent()
                        .toBuilder()
                        .id(null)
                        .build())
                .block();

        CardEntity savedCard = cardRepository
                .save(DataUtils.getCardPersistent()
                        .toBuilder()
                        .id(null)
                        .customerId(savedCustomer.getId())
                        .build())
                .block();
        TransactionDtoInput transactionDtoInput = DataUtils.geTransactionDtoInput();

        //when
        WebTestClient.ResponseSpec result = webTestClient.post()
                .uri("/api/v1/payments/transaction")
                .header("Authorization", "Basic bWVyY2hhbnROYW1lOnBhc3M=")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(transactionDtoInput), TransactionDtoInput.class)
                .exchange();

        //then
        result.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.transaction_id").isNotEmpty()
                .jsonPath("$.status").isEqualTo("IN_PROGRESS")
                .jsonPath("$.message").isEqualTo("OK");
    }

    @Test
    @DisplayName("Test create transaction faild functionality")
    public void givenIncorrectDtoInput_whenCreate_thenExceptionIsReturned() {

        //given
        TransactionDtoInput transactionDtoInput = DataUtils.geTransactionDtoInput();

        //when
        WebTestClient.ResponseSpec result = webTestClient.post()
                .uri("/api/v1/payments/transaction")
                .header("Authorization", "Basic bWVyY2hhbnROYW1lOnBhc3M=")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(transactionDtoInput), TransactionDtoInput.class)
                .exchange();

        //then
        result.expectStatus().isBadRequest()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.status").isEqualTo("FAILED")
                .jsonPath("$.message").isEqualTo("PAYMENT_METHOD_NOT_ALLOWED");
    }
}
