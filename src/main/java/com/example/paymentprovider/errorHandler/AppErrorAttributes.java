package com.example.paymentprovider.errorHandler;

import com.example.paymentprovider.dto.TransactionDtoOut;
import com.example.paymentprovider.entity.TransactionStatus;
import com.example.paymentprovider.exceptions.TransactionFailedException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Component
public class AppErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options){
        var errorAttributes = super.getErrorAttributes(request, options);
        var error = getError(request);

        if(error instanceof TransactionFailedException){
            errorAttributes.put("status", HttpStatus.BAD_REQUEST);
            if(((TransactionFailedException) error).getErrorCode().equals("TOPUP_FAILED")){
                errorAttributes.put("body", new TransactionDtoOut(null, TransactionStatus.FAILED, "PAYMENT_METHOD_NOT_ALLOWED"));
            }else{
                errorAttributes.put("body", new TransactionDtoOut(null, TransactionStatus.FAILED, "PAYOUT_MIN_AMOUNT"));
            }
        }else{
            errorAttributes.put("status", HttpStatus.NOT_FOUND);
            errorAttributes.put("body", "Transactions not found");
        }
        return errorAttributes;
    }
}
