package com.project.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.config.exception.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The AuthEntryPointJwt class provided is an implementation of the AuthenticationEntryPoint interface in Spring Security.
 * This class is responsible for handling authentication failures and responding with an appropriate error message
 */

@Component
@Slf4j
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        log.error("EntryPoint Unauthorized Error");

        // Create the ExceptionResponse object
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized",
                "Access denied. Please provide valid authentication."
        );

        // Configure the response to return JSON
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(convertObjectToJson(exceptionResponse));
    }

    private String convertObjectToJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
