package com.userdata.api.error;

public class BusinessException extends RuntimeException {

    public BusinessException(final String message) {
        super(message);
    }
}