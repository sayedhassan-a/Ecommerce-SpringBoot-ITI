package org.example.ecommerce.system.exceptions.exceptionHandling.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message);
    }
}
