package com.example.paymentprovider.service.impl;

import com.example.paymentprovider.dto.TransactionDtoInput;
import com.example.paymentprovider.entity.Country;
import com.example.paymentprovider.entity.CustomerEntity;
import com.example.paymentprovider.entity.Status;
import com.example.paymentprovider.mappers.CustomerMapper;
import com.example.paymentprovider.repository.CustomerRepository;
import com.example.paymentprovider.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Mono<CustomerEntity> findById(String id) {
        return customerRepository.findById(id);
    }

    @Override
    public Mono<CustomerEntity> findByFirstNameAndLastNameAndCountry(TransactionDtoInput dtoInput) {
        return customerRepository
                .findByFirstNameAndLastNameAndCountry(dtoInput.customer().firstName(),
                        dtoInput.customer().lastName(),
                        dtoInput.customer().country())
                .switchIfEmpty(Mono.defer(() -> customerRepository.save(customerMapper.map(dtoInput.customer())
                        .toBuilder()
                        .createdAt(LocalDateTime.now())
                        .updateAt(LocalDateTime.now())
                        .status(Status.ACTIVE)
                        .build())));
    }
}
