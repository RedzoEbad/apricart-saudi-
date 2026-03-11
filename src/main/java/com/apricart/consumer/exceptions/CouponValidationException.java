package com.apricart.consumer.exceptions;

import com.apricart.consumer.security.dto.response.CouponResponseDTO;
import lombok.Getter;
import org.apache.poi.ss.formula.functions.T;

@Getter
public class CouponValidationException extends RuntimeException {
    private final String errorCode;

    public CouponValidationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}

