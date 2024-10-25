package org.example.ecommerce.exceptionHandling.exceptionHandler;

import org.example.ecommerce.exceptionHandling.errorResponse.ErrorResponse;
import org.example.ecommerce.exceptionHandling.exception.UnAuthorizedAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class UnAuthorizedAccessExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(UnAuthorizedAccessException exc){
        ErrorResponse error = new ErrorResponse();
        error.setStatus(HttpStatus.FORBIDDEN.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(LocalDateTime.now());
        return new ResponseEntity<>(error,HttpStatus.FORBIDDEN);
    }
}
