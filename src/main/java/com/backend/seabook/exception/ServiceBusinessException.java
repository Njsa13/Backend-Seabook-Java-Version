package com.backend.seabook.exception;

public class ServiceBusinessException extends RuntimeException {
    public ServiceBusinessException(String message) {
        super(message);
    }
}
