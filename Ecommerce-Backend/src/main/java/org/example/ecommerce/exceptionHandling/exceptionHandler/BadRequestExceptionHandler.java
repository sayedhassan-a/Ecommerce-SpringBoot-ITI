package org.example.ecommerce.exceptionHandling.exceptionHandler;

import org.example.ecommerce.exceptionHandling.errorResponse.ErrorResponse;
import org.example.ecommerce.exceptionHandling.errorResponse.ValidationResponse;
import org.example.ecommerce.exceptionHandling.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class BadRequestExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(BadRequestException exc) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<ValidationResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exc) {
        ValidationResponse error = new ValidationResponse();
        Map<String, String> errors = new HashMap<>();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        exc.getBindingResult().getAllErrors().forEach((err) -> {
            String fieldName = ((FieldError) err).getField();
            String errorMessage = err.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        error.setTimeStamp(LocalDateTime.now());
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }
}
