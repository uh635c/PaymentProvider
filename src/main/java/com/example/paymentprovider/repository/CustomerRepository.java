package com.example.paymentprovider.repository;

import com.example.paymentprovider.entity.Country;
import com.example.paymentprovider.entity.CustomerEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends R2dbcRepository<CustomerEntity, String> {

        @Query("select id, first_name, last_name, country, created_at, updated_at, status from customers where customers.first_name = :firstName AND customers.last_name = :lastName AND customers.country = :country")
        Mono<CustomerEntity> findByFirstNameAndLastNameAndCountry(String firstName, String lastName, Country country);
}
