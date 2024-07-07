package com.example.paymentprovider.exceptions;

import lombok.Getter;

public class TransactionFailedException extends RuntimeException{

    @Getter
    protected String errorCode;

    public TransactionFailedException(String message, String errorCode){
        super(message);
        this.errorCode = errorCode;
    }

}
