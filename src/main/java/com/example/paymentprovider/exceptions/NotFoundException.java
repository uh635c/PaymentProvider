package com.example.paymentprovider.exceptions;

import lombok.Getter;

public class NotFoundException extends RuntimeException {

    @Getter
    protected String errorCode;

    public NotFoundException(String message, String errorCode){
        super(message);
        this.errorCode = errorCode;
    }

}
