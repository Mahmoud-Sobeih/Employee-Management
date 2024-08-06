package com.project.config.exception.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String msg){
        super(msg);
    }
}
