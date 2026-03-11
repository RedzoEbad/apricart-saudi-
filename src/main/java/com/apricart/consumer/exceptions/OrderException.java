package com.apricart.consumer.exceptions;

import lombok.Getter;

@Getter
public class OrderException extends RuntimeException {

    public OrderException(String message) {
        super(message);
    }

}

