package com.project.config.security;

import com.project.config.exception.exceptions.AuthenticationException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer") && requestTokenHeader.length() > 7) {

            String JWTToken = requestTokenHeader.substring(7);
            try {
                // Check expiration of token
                if (jwtTokenUtil.validateToken(JWTToken)) {
                    String email = jwtTokenUtil.getUsernameFromToken(JWTToken);
                    UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // After setting the Authentication in the context, we specify
                    // that the current user is authenticated. So it passes the
                    // Spring Security Configurations successfully.
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }

            } catch (UsernameNotFoundException ex) {
                log.error("UserName not found Exception", ex);
                throw new UsernameNotFoundException("Invalid userName or password!!");
            }

        } else {
            log.info("User didn't have JWT Token");
        }

        filterChain.doFilter(request, response);
    }
}
