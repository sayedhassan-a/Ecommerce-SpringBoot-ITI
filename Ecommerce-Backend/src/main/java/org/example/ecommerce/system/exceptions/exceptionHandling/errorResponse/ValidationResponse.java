package org.example.ecommerce.system.exceptions.exceptionHandling.errorResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationResponse {
    private int status;
    private Map<String,String> errors;
    private LocalDateTime timeStamp;
}
