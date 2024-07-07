package com.example.paymentprovider.service;

import com.example.paymentprovider.dto.CustomerDto;
import com.example.paymentprovider.dto.TransactionDtoInput;
import com.example.paymentprovider.entity.Country;
import com.example.paymentprovider.entity.CustomerEntity;
import com.example.paymentprovider.mappers.CustomerMapper;
import com.example.paymentprovider.mappers.CustomerMapperImpl;
import com.example.paymentprovider.repository.CustomerRepository;
import com.example.paymentprovider.service.impl.CustomerServiceImpl;
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

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    CustomerRepository customerRepository;
    @Spy
    CustomerMapperImpl customerMapper;

    @InjectMocks
    CustomerServiceImpl customerService;

    @Test
    @DisplayName("Test find customer by name, last name and country functionality")
    public void givenFirstNameAndLastNameAndCountry_whenFindByFirstNameAndLastNameAndCountry_thenSuccessReturned(){

        //given
        TransactionDtoInput dtoInput = DataUtils.geTransactionDtoInput();
        CustomerEntity customerEntity = DataUtils.getCustomerPersistent();

        BDDMockito.given(customerRepository.findByFirstNameAndLastNameAndCountry(anyString(), anyString(), any(Country.class)))
                .willReturn(Mono.just(customerEntity));

        //when
        Mono<CustomerEntity> obtainedCustomer = customerService.findByFirstNameAndLastNameAndCountry(dtoInput);

        //then
        StepVerifier.create(obtainedCustomer)
                .assertNext(customer ->{
                    verify(customerRepository, times(1)).findByFirstNameAndLastNameAndCountry(
                            anyString(), anyString(), any(Country.class));
                    verify(customerRepository, never()).save(any(CustomerEntity.class));
                    assertThat(customer).isEqualTo(customer);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Test find customer by  incorrect name, last name and country functionality")
    public void givenIncorrectFirstNameOrLastNameOrCountry_whenFindByFirstNameAndLastNameAndCountry_thenSuccessReturned(){

        //given
        TransactionDtoInput dtoInput = DataUtils.geTransactionDtoInput();
        CustomerEntity customerEntity = DataUtils.getCustomerPersistent();

        BDDMockito.given(customerRepository.findByFirstNameAndLastNameAndCountry(anyString(), anyString(), any(Country.class)))
                .willReturn(Mono.empty());
        BDDMockito.given(customerRepository.save(any(CustomerEntity.class)))
                .willReturn(Mono.just(customerEntity));

        //when
        Mono<CustomerEntity> obtainedCustomer = customerService.findByFirstNameAndLastNameAndCountry(dtoInput);

        //then
        StepVerifier.create(obtainedCustomer)
                .assertNext(customer ->{
                    verify(customerRepository, times(1)).findByFirstNameAndLastNameAndCountry(
                            anyString(), anyString(), any(Country.class));
                    verify(customerRepository, times(1)).save(any(CustomerEntity.class));
                    verify(customerMapper, times(1)).map(any(CustomerDto.class));
                    assertThat(customer).isEqualTo(customer);
                })
                .verifyComplete();
    }
}
