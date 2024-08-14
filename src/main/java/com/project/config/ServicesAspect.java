package com.project.config;

import com.project.DTO.LoginRequest;
import com.project.DTO.SignupRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ServicesAspect {

    @Before(value = "execution(* com.project.service.*.*(..)) && args(request)")
    public <T> void beforeEmployeeServiceWithParameters(JoinPoint joinPoint, T request) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Before Advice {}", methodName);
        if (methodName.equals("login")) {
            LoginRequest loginRequest = (LoginRequest) request;
            log.info("Username: {}", loginRequest.getUserName());
        } else if (methodName.equals("signup")) {
            SignupRequest signupRequest = (SignupRequest) request;
            log.info("Username: {}", signupRequest.getEmail());
        } else {
            log.info("Request: {}", request.toString());
        }
    }

    @Before(value = "execution(* com.project.service.*.*(..))")
    public void beforeEmployeeServiceWithoutParameters(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        if(methodName.equals("getAllEmployees")) {
            log.info("Before Advice {}", methodName);
        }
    }

    @AfterReturning(value = "execution(* com.project.service.*.*(..))", returning = "response")
    public void afterEmployeeService(JoinPoint joinPoint, Object response) {
        log.info("After Advice {}", joinPoint.getSignature().getName());
        log.info("Response: {}", response.toString());
    }
}
