package org.example.ecommerce.system.exceptions.exceptionHandling.exception;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}
