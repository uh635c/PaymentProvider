package com.example.paymentprovider.service;


import com.example.paymentprovider.entity.MerchantEntity;
import com.example.paymentprovider.entity.Status;
import com.example.paymentprovider.repository.MerchantRepository;
import com.example.paymentprovider.service.impl.MerchantServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;


@ExtendWith(MockitoExtension.class)
public class MerchantServiceImplTest {

    @Mock
    private MerchantRepository merchantRepository;

    @InjectMocks
    private MerchantServiceImpl serviceUnderTest;

    @Test
    @DisplayName("Test get merchant by name functionality")
    public void givenMerchantName_whenGetByName_thenMerchantIsReturned(){
        //given
        MerchantEntity entity = MerchantEntity.builder()
                .id(UUID.randomUUID().toString())
                .name("Jhon")
                .secretKey(Base64.getEncoder().encodeToString("secretKey".getBytes()))
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
        BDDMockito.given(merchantRepository.findByName(anyString()))
                .willReturn(Mono.just(entity));

        //when
        Mono<MerchantEntity> obtainedEntity = serviceUnderTest.getByName("Jhon");
        //then
        assertThat(obtainedEntity).isNotNull();
    }
}
