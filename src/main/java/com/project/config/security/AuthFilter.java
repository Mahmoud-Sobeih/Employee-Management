package com.project.config.security;

import com.project.config.exception.exceptions.AuthenticationException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@Slf4j
public class AuthFilter extends OncePerRequestFilter {

    private final JWTTokenUtil jwtTokenUtil;
    private final JWTUserDetailsService jwtUserDetailsService;

    public AuthFilter(JWTTokenUtil jwtTokenUtil, JWTUserDetailsService jwtUserDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String email;
        String JWTToken;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer") && requestTokenHeader.length() > 7) {
            JWTToken = requestTokenHeader.substring(7);
            try {

                email = jwtTokenUtil.getUsernameFromToken(JWTToken);

                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(email);

                    // Check expiration of token
                    if (jwtTokenUtil.validateToken(JWTToken, userDetails)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken
                                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        // After setting the Authentication in the context, we specify
                        // that the current user is authenticated. So it passes the
                        // Spring Security Configurations successfully.
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }else {
                        log.error("JWT Token has expired");
                        throw new AuthenticationException("JWT Token has expired");
                    }
                }
            } catch (IllegalArgumentException e) {
                log.error("AuthException-IllegalArgumentException", e);
                throw new AuthenticationException("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {//
                log.error("AuthException-ExpiredJwtException", e);
                throw new AuthenticationException("JWT Token has expired");
            }
        }else{
            log.info("User didn't have JWT Token");
        }

        filterChain.doFilter(request, response);
    }
}
