package com.example.paymentprovider.service.impl;

import com.example.paymentprovider.dto.TransactionDtoInput;
import com.example.paymentprovider.entity.CardEntity;
import com.example.paymentprovider.entity.Status;
import com.example.paymentprovider.mappers.CardMapper;
import com.example.paymentprovider.repository.CardRepository;
import com.example.paymentprovider.service.CardService;
import com.example.paymentprovider.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CustomerService customerService;
    private final CardMapper cardMapper;

    @Override
    public Mono<CardEntity> findById(String id) {
        return cardRepository.findById(id);
    }

    @Override
    public Mono<CardEntity> findByCardNumber(TransactionDtoInput dtoInput) {
        return cardRepository.findByCardNumber(dtoInput.cardData().cardNumber())
                .switchIfEmpty(Mono.defer(()-> customerService.findByFirstNameAndLastNameAndCountry(dtoInput)
                                .flatMap(cstm -> {
                                    CardEntity card = cardMapper.mapToEntity(dtoInput.cardData()).toBuilder()
                                            .createdAt(LocalDateTime.now())
                                            .updateAt(LocalDateTime.now())
                                            .status(Status.ACTIVE)
                                            .currency(dtoInput.currency())
                                            .customerId(cstm.getId())
                                            .build();
                                    return cardRepository.save(card);
                                })));
    }

    @Override
    public Mono<CardEntity> findByCardNumber(Long cardNumber) {
        return cardRepository.findByCardNumber(cardNumber);
    }
}
