package com.example.paymentprovider.service;

import com.example.paymentprovider.entity.AccountEntity;
import com.example.paymentprovider.entity.Currency;
import com.example.paymentprovider.repository.AccountRepository;
import com.example.paymentprovider.service.impl.AccountServiceImpl;
import com.example.paymentprovider.util.DataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountServiceUnderTest;


    @Test
    @DisplayName("Test get account by currency and merchant id functionality")
    public void givenMerchantIdAndCurrency_whenGetByCurrencyAndMerchantId_givenReturnSuccess(){

        //given
        AccountEntity entity = DataUtils.getAccount1ForMerchant1Persistent();
        BDDMockito.given(accountRepository.findByCurrencyAndMerchantId(any(), anyString()))
                .willReturn(Mono.just(entity));

        //when
        Mono<AccountEntity> obtainedEntity = accountServiceUnderTest.getByCurrencyAndMerchantId(Currency.EUR, "merchantId");

        //given
        StepVerifier.create(obtainedEntity)
                .expectNext(entity)
                .verifyComplete();
    }



//    @Test
//    @DisplayName("Test get accounts by merchant id functionality")
//    public void givenMerchantId_whenGetAllByMerchantId_thenReturnSuccess(){
//
//        //given
//        AccountEntity entity1 = DataUtils.getAccount1ForMerchant1Persistent();
//        AccountEntity entity2 = DataUtils.getAccount2ForMerchant1Persistent();
//        AccountEntity entity3 = DataUtils.getAccount3ForMerchant1Persistent();
//
//        BDDMockito.given(accountRepository.findAllByMerchantId(anyString()))
//                .willReturn(Flux.just(entity1, entity2, entity3));
//
//        //when
//        Flux<AccountEntity> obtainedMerchants = accountServiceUnderTest.getAllByMerchantId("merchant1");
//
//        //then
//        StepVerifier.create(obtainedMerchants)
//                .expectNextCount(3)
//                .verifyComplete();
//    }
}
