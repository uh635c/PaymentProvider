package com.example.paymentprovider.service;


import com.example.paymentprovider.dto.TransactionDto;
import com.example.paymentprovider.dto.TransactionDtoInput;
import com.example.paymentprovider.dto.TransactionDtoOut;
import com.example.paymentprovider.entity.*;
import com.example.paymentprovider.exceptions.TransactionFailedException;
import com.example.paymentprovider.mappers.CustomerMapper;
import com.example.paymentprovider.mappers.CustomerMapperImpl;
import com.example.paymentprovider.mappers.TransactionMapper;
import com.example.paymentprovider.mappers.TransactionMapperImpl;
import com.example.paymentprovider.repository.TransactionRepository;
import com.example.paymentprovider.service.impl.TransactionServiceImpl;
import com.example.paymentprovider.util.DataUtils;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private CardService cardService;
    @Mock
    private CustomerService customerService;
    @Spy
    private TransactionMapper transactionMapper = new TransactionMapperImpl();
    @Spy
    private CustomerMapper customerMapper = new CustomerMapperImpl();

    @Mock
    private AccountService accountService;
    @Mock
    private NotificationService notificationService;

    private static MockWebServer mockBackEnd= new MockWebServer();
    String str = mockBackEnd.url("/v1/test").toString();

    @Spy
    private WebClient webClient = WebClient.builder()
            .baseUrl(str)
            .build();
    @Mock
    private TransactionalOperator operator;


    @InjectMocks
    private TransactionServiceImpl transactionServiceUnderTest;



    @Test
    @DisplayName("Test get transaction by uid and type functionality")
    public void givenTransactionUidAndType_whenFindByIdAndType_thenReturnSuccess() {

        //given
        String transactionId = "transactionId";
        TransactionType type = TransactionType.PAYOUT;
        TransactionEntity transaction = DataUtils.getTransactionPersistant();

        String cardId = "cardId";
        CardEntity card = DataUtils.getCardPersistent();

        String customerId = "customerId";
        CustomerEntity customer = DataUtils.getCustomerPersistent();

        BDDMockito.given(transactionRepository.findByIdAndType(anyString(), any(TransactionType.class)))
                .willReturn(Mono.just(transaction));
        BDDMockito.given(cardService.findById(anyString()))
                .willReturn(Mono.just(card));
        BDDMockito.given(customerService.findById(anyString()))
                .willReturn(Mono.just(customer));

        //when
        Mono<TransactionDto> obtainedTransactionDto = transactionServiceUnderTest.getByUidAndType(transactionId, type);

        //given
        StepVerifier.create(obtainedTransactionDto)
                .assertNext(dto -> {
                    verify(transactionRepository, times(1)).findByIdAndType(anyString(), any(TransactionType.class));
                    verify(cardService, times(1)).findById(anyString());
                    verify(customerService, times(1)).findById(anyString());
                    transaction.setCard(card);
                    transaction.setCustomer(customer);
                    assertThat(dto).isEqualTo(transactionMapper.mapToDto(transaction));
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Test get transaction by incorrect uid or incorrect type functionality")
    public void givenIncorrectUidAndType_whenFindByIdAndType_thenExceptionReturned() {

        //given
        String transactionId = "transactionId";
        TransactionType type = TransactionType.PAYOUT;

        BDDMockito.given(transactionRepository.findByIdAndType(anyString(), any(TransactionType.class)))
                .willReturn(Mono.empty());

        //when
        Mono<TransactionDto> obtainedTransactionDto = transactionServiceUnderTest.getByUidAndType(transactionId, type);

        //given
        StepVerifier.create(obtainedTransactionDto)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    @DisplayName("Test get transactions by time period and type functionality")
    public void givenStartAndEndPeriodAndType_whenGetByStartAndEndDateAndType_thenSuccessReturn() {

        //given
        String transactionId1 = "transactionId";
        String transactionId2 = "transactionId2";
        TransactionType type = TransactionType.PAYOUT;
        TransactionEntity transaction1 = DataUtils.getTransactionPersistant();
        TransactionEntity transaction2 = DataUtils.getTransaction2Persistant();

        String cardId1 = "cardId";
        String cardId2 = "cardId2";
        CardEntity card1 = DataUtils.getCardPersistent();
        CardEntity card2 = DataUtils.getCard2Persistent();

        String customerId1 = "customerId";
        String customerId2 = "customerId2";
        CustomerEntity customer1 = DataUtils.getCustomerPersistent();
        CustomerEntity customer2 = DataUtils.getCustomer2Persistent();


        BDDMockito.given(transactionRepository.findAllByCreatedAtBetweenAndType(any(LocalDateTime.class), any(LocalDateTime.class), any(TransactionType.class)))
                .willReturn(Flux.just(transaction1, transaction2));
        BDDMockito.given(cardService.findById(cardId1))
                .willReturn(Mono.just(card1));
        BDDMockito.given(cardService.findById(cardId2))
                .willReturn(Mono.just(card2));
        BDDMockito.given(customerService.findById(customerId1))
                .willReturn(Mono.just(customer1));
        BDDMockito.given(customerService.findById(customerId2))
                .willReturn(Mono.just(customer2));

        //when
        Flux<TransactionDto> obtainedTransactions = transactionServiceUnderTest.getByStartAndEndDateAndType(123L, 345L, type);

        //then
        StepVerifier.create(obtainedTransactions)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    @DisplayName("Test get transactions by incorrect input functionality")
    public void givenIncorrectStartOrEndPeriodOrType_whenGetByStartAndEndDateAndType_thenExceptoinReturned() {

        //given
        TransactionType type = TransactionType.PAYOUT;
        BDDMockito.given(transactionRepository.findAllByCreatedAtBetweenAndType(any(LocalDateTime.class), any(LocalDateTime.class), any(TransactionType.class)))
                .willReturn(Flux.empty());

        //when
        Flux<TransactionDto> obtainedTransactions = transactionServiceUnderTest.getByStartAndEndDateAndType(123L, 345L, type);

        //then
        StepVerifier.create(obtainedTransactions)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    @DisplayName("Test create topup transaction functionality")
    public void givenInputDto_whenCreateTopup_thenReturnSuccess() {

        //given
        TransactionDtoInput transactionDto = DataUtils.geTransactionDtoInput();
        TransactionEntity transaction = DataUtils.getTransactionPersistant();
        AccountEntity account = DataUtils.getAccount1ForMerchant1Persistent();
        CardEntity card = DataUtils.getCardPersistent();

        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.post("https://ma.com").body("mock"));
        exchange.getAttributes().put("merchantId", "merchantId");


        BDDMockito.given(accountService.getByCurrencyAndMerchantId(any(Currency.class), anyString()))
                .willReturn(Mono.just(account));
        BDDMockito.given(cardService.findByCardNumber(any(TransactionDtoInput.class)))
                .willReturn(Mono.just(card));
        BDDMockito.given(transactionRepository.save(any(TransactionEntity.class)))
                .willReturn(Mono.just(transaction));

        //when
        Mono<TransactionDtoOut> obtainedTransactionDto = transactionServiceUnderTest.createTopup(transactionDto, exchange);

        //then
        StepVerifier.create(obtainedTransactionDto)
                .assertNext(dto -> {
                    verify(transactionRepository, times(1)).save(any(TransactionEntity.class));
                    verify(cardService, times(1)).findByCardNumber(any(TransactionDtoInput.class));
                    verify(accountService, times(1)).getByCurrencyAndMerchantId(any(Currency.class), anyString());
                    transaction.setCard(card);
                    transaction.setCustomer(customerMapper.map(transactionDto.customer()));
                    assertThat(dto).isEqualTo(transactionMapper.mapToDtoOut(transaction));
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Test create topup transaction functionality")
    public void givenIncorrectInputDto_whenCreateTopup_thenReturnException() {

        //given
        TransactionDtoInput transactionDto = DataUtils.geTransactionDtoInput();
        CardEntity card = DataUtils.getCardPersistent();

        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.post("https://ma.com").body("mock"));
        exchange.getAttributes().put("merchantId", "merchantId");


        BDDMockito.given(accountService.getByCurrencyAndMerchantId(any(Currency.class), anyString()))
                .willReturn(Mono.empty());
        BDDMockito.given(cardService.findByCardNumber(any(TransactionDtoInput.class)))
                .willReturn(Mono.just(card));

        //when
        Mono<TransactionDtoOut> obtainedTransactionDto = transactionServiceUnderTest.createTopup(transactionDto, exchange);

        //then
        StepVerifier.create(obtainedTransactionDto)
                .expectError(TransactionFailedException.class)
                .verify();
    }


    @Test
    @DisplayName("Test create payout transaction functionality")
    public void givenInputDto_whenCreatePayout_thenReturnSuccess() {

        //given
        TransactionDtoInput transactionDto = DataUtils.geTransactionDtoInput();
        TransactionEntity transaction = DataUtils.getTransactionPersistant();
        AccountEntity account = DataUtils.getAccount1ForMerchant1Persistent();
        AccountEntity updateAccount = account.toBuilder()
                .balance(account.getBalance() - transactionDto.amount())
                .build();
        CardEntity card = DataUtils.getCardPersistent();

        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.post("https://ma.com").body("mock"));
        exchange.getAttributes().put("merchantId", "merchantId");

        BDDMockito.given(accountService.getByCurrencyAndMerchantId(any(Currency.class), anyString()))
                .willReturn(Mono.just(account));
        BDDMockito.given(accountService.save(any(AccountEntity.class)))
                .willReturn(Mono.just(updateAccount));
        BDDMockito.given(cardService.findByCardNumber(anyLong()))
                .willReturn(Mono.just(card));
        BDDMockito.given(transactionRepository.save(any(TransactionEntity.class)))
                .willReturn(Mono.just(transaction));

        //when
        Mono<TransactionDtoOut> obtainedTransactionDto = transactionServiceUnderTest.createPayout(transactionDto, exchange);

        //then
        StepVerifier.create(obtainedTransactionDto)
                .assertNext(dto -> {
                    verify(transactionRepository, times(1)).save(any(TransactionEntity.class));
                    verify(cardService, times(1)).findByCardNumber(anyLong());
                    verify(accountService, times(1)).getByCurrencyAndMerchantId(any(Currency.class), anyString());
                    verify(accountService, times(1)).save(any(AccountEntity.class));
                    transaction.setCard(card);
                    transaction.setCustomer(customerMapper.map(transactionDto.customer()));
                    assertThat(dto).isEqualTo(transactionMapper.mapToDtoOut(transaction));
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Test create payout transaction when balance is not enough functionality")
    public void givenInputDtoWithAmountNotEnough_whenCreatePayout_thenReturnException() {

        //given
        TransactionDtoInput transactionDto = DataUtils.geTransactionDtoInput().toBuilder().amount(1001L).build();
        AccountEntity account = DataUtils.getAccount1ForMerchant1Persistent();
        CardEntity card = DataUtils.getCardPersistent();

        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.post("https://ma.com").body("mock"));
        exchange.getAttributes().put("merchantId", "merchantId");

        BDDMockito.given(accountService.getByCurrencyAndMerchantId(any(Currency.class), anyString()))
                .willReturn(Mono.just(account));
        BDDMockito.given(cardService.findByCardNumber(anyLong()))
                .willReturn(Mono.just(card));

        //when
        Mono<TransactionDtoOut> obtainedTransactionDto = transactionServiceUnderTest.createPayout(transactionDto, exchange);

        //then
        StepVerifier.create(obtainedTransactionDto)
                .verifyErrorMessage("Not enough money on the balance");
    }

    @Test
    @DisplayName("Test create payout transaction when account not found functionality")
    public void givenInputDtoWithAmountNotEnougth_whenCreatePayout_thenReturnException() {

        //given
        TransactionDtoInput transactionDto = DataUtils.geTransactionDtoInput().toBuilder().amount(1001L).build();
        CardEntity card = DataUtils.getCardPersistent();

        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.post("https://ma.com").body("mock"));
        exchange.getAttributes().put("merchantId", "merchantId");

        BDDMockito.given(accountService.getByCurrencyAndMerchantId(any(Currency.class), anyString()))
                .willReturn(Mono.empty());
        BDDMockito.given(cardService.findByCardNumber(anyLong()))
                .willReturn(Mono.just(card));

        //when
        Mono<TransactionDtoOut> obtainedTransactionDto = transactionServiceUnderTest.createPayout(transactionDto, exchange);

        //then
        StepVerifier.create(obtainedTransactionDto)
                .verifyErrorMessage("Merchant is not found");
    }

    @Test
    @DisplayName("Test create payout transaction when card not found functionality")
    public void givenInputDtoWithNewCard_whenCreatePayout_thenReturnException() {

        //given
        TransactionDtoInput transactionDto = DataUtils.geTransactionDtoInput().toBuilder().amount(1001L).build();
        AccountEntity account = DataUtils.getAccount1ForMerchant1Persistent();
        AccountEntity updateAccount = account.toBuilder()
                .balance(account.getBalance() - transactionDto.amount())
                .build();

        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.post("https://ma.com").body("mock"));
        exchange.getAttributes().put("merchantId", "merchantId");

        BDDMockito.given(accountService.getByCurrencyAndMerchantId(any(Currency.class), anyString()))
                .willReturn(Mono.just(account));
        BDDMockito.given(cardService.findByCardNumber(anyLong()))
                .willReturn(Mono.empty());

        //when
        Mono<TransactionDtoOut> obtainedTransactionDto = transactionServiceUnderTest.createPayout(transactionDto, exchange);

        //then
        StepVerifier.create(obtainedTransactionDto)
                .verifyErrorMessage("Card is not found");
    }

    @Test
    @DisplayName("Test notification processor functionality")
    public void givenListTransactions_whenNotificationProcessor_thenSendNotifications() throws InterruptedException, IOException {

        //given
        TransactionEntity transactionEntity = DataUtils.getTransactionPersistantForNotification(str);
        CardEntity cardEntity = DataUtils.getCardPersistent();
        CustomerEntity customerEntity = DataUtils.getCustomerPersistent();
        AccountEntity accountEntity = DataUtils.getAccount1ForMerchant1Persistent();
        AccountEntity updatedAccount = accountEntity.toBuilder()
                .balance(accountEntity.getBalance() + transactionEntity.getAmount())
                .build();
        TransactionEntity updatedTransaction = transactionEntity.toBuilder()
                .status(TransactionStatus.COMPLETED)
                .updatedAt(LocalDateTime.now())
                .build();
        NotificationEntity notificationEntity = DataUtils.getNotificationEntityPersistentFail();

        BDDMockito.given(transactionRepository.findAllByStatus(any(TransactionStatus.class)))
                .willReturn(Flux.just(transactionEntity));
        BDDMockito.given(cardService.findById(anyString()))
                .willReturn(Mono.just(cardEntity));
        BDDMockito.given(customerService.findById(anyString()))
                .willReturn(Mono.just(customerEntity));
        BDDMockito.given(accountService.getById(anyString()))
                .willReturn(Mono.just(accountEntity));
        BDDMockito.given(accountService.save(any(AccountEntity.class)))
                .willReturn(Mono.just(updatedAccount));
        BDDMockito.given(transactionRepository.save(any(TransactionEntity.class)))
                .willReturn(Mono.just(updatedTransaction));
        BDDMockito.given(notificationService.save(any(NotificationEntity.class)))
                .willReturn(Mono.just(notificationEntity));
        BDDMockito.given(operator.transactional(any(Mono.class)))
                        .willAnswer(invocation -> {
                            Object[] args = invocation.getArguments();
                            return args[0];
                        });

        mockBackEnd.enqueue(new MockResponse().setResponseCode(HttpStatus.BAD_REQUEST.value())
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setBody("1 try"));
        mockBackEnd.enqueue(new MockResponse().setResponseCode(HttpStatus.BAD_REQUEST.value())
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setBody("2 try"));
        mockBackEnd.enqueue(new MockResponse().setResponseCode(HttpStatus.OK.value())
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setBody("3 try"));


        //when
//        mockBackEnd.start();
        Mono<Void> obtainedVoid = transactionServiceUnderTest.notificationProcessor();

        //then

        StepVerifier.create(obtainedVoid)
                .verifyComplete();
        verify(transactionRepository, times(1)).findAllByStatus(any(TransactionStatus.class));
        verify(cardService, times(1)).findById(anyString());
        verify(customerService, times(1)).findById(anyString());
        verify(accountService, times(1)).getById(anyString());
        verify(accountService, times(1)).save(any(AccountEntity.class));
        verify(transactionRepository, times(1)).save(any(TransactionEntity.class));
        verify(notificationService, times(3)).save(any(NotificationEntity.class));

        mockBackEnd.shutdown();

    }

}
