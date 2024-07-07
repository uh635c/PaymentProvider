package com.example.paymentprovider.service;

import com.example.paymentprovider.dto.CardDto;
import com.example.paymentprovider.dto.TransactionDtoInput;
import com.example.paymentprovider.entity.CardEntity;
import com.example.paymentprovider.entity.CustomerEntity;
import com.example.paymentprovider.mappers.CardMapperImpl;
import com.example.paymentprovider.repository.CardRepository;
import com.example.paymentprovider.service.impl.CardServiceImpl;
import com.example.paymentprovider.util.DataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceImplTest {

    @Mock
    CustomerService customerService;
    @Mock
    CardRepository cardRepository;
    @Spy
    CardMapperImpl cardMapper;

    @InjectMocks
    CardServiceImpl cardService;

    @Test
    @DisplayName("Test find card by card number functionality")
    public void givenCardNumber_whenFindByCardNumber_thenSuccessReturned(){

        //given
        CardEntity cardEntity = DataUtils.getCardPersistent();
        TransactionDtoInput dtoInput = DataUtils.geTransactionDtoInput();

        BDDMockito.given(cardRepository.findByCardNumber(anyLong()))
                .willReturn(Mono.just(cardEntity));


        //when
        Mono<CardEntity> obtainedCard = cardService.findByCardNumber(dtoInput);

        //then
        StepVerifier.create(obtainedCard)
                .assertNext(card ->{
                    verify(cardRepository, times(1)).findByCardNumber(anyLong());
                    verify(customerService, never()).findByFirstNameAndLastNameAndCountry(any(TransactionDtoInput.class));
                    assertThat(card).isEqualTo(cardEntity);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Test find card by incorrect card number functionality")
    public void givenIncorrectCardNumber_whenFindByCardNumber_thenSuccessReturned(){

        //given
        CardEntity cardEntity = DataUtils.getCardPersistent();
        CustomerEntity customerEntity = DataUtils.getCustomerPersistent();
        TransactionDtoInput dtoInput = DataUtils.geTransactionDtoInput();

        BDDMockito.given(cardRepository.findByCardNumber(anyLong()))
                .willReturn(Mono.empty());
        BDDMockito.given(customerService.findByFirstNameAndLastNameAndCountry(any(TransactionDtoInput.class)))
                .willReturn(Mono.just(customerEntity));
        BDDMockito.given(cardRepository.save(any(CardEntity.class)))
                .willReturn(Mono.just(cardEntity));

        //when
        Mono<CardEntity> obtainedCard = cardService.findByCardNumber(dtoInput);

        //then
        StepVerifier.create(obtainedCard)
                .assertNext(card ->{
                    verify(cardRepository, times(1)).findByCardNumber(anyLong());
                    verify(customerService, times(1)).findByFirstNameAndLastNameAndCountry(any(TransactionDtoInput.class));
                    verify(cardRepository, times(1)).save(any(CardEntity.class));
                    verify(cardMapper, times(1)).mapToEntity(any(CardDto.class));
                    assertThat(card.getCardNumber()).isEqualTo(123456789L);
                })
                .verifyComplete();
    }

}
