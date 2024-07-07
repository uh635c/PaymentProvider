package com.example.paymentprovider.mappers;

import com.example.paymentprovider.dto.CustomerDto;
import com.example.paymentprovider.entity.CustomerEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDto map(CustomerEntity entity);

    @InheritInverseConfiguration
    CustomerEntity map(CustomerDto dto);


}
