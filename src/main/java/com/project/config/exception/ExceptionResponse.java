package com.project.config.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionResponse {
    
    private int status;
    private String error;
    private String message;
}
