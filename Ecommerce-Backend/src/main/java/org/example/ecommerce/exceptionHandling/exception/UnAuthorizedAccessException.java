package org.example.ecommerce.exceptionHandling.exception;

import lombok.Data;

public class UnAuthorizedAccessException extends RuntimeException{
    public UnAuthorizedAccessException(String message) {
        super(message);
    }
}
