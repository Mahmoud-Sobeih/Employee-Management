package com.project.config.security;

import com.project.DTO.LoginRequest;
import com.project.DTO.LoginResponse;
import com.project.config.exception.exceptions.AuthenticationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JWTTokenUtil {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.token.expiration.in.seconds}")
    private long expirationDuration;

    private final Clock clock = DefaultClock.INSTANCE;

    public LoginResponse generateToken(LoginRequest request) {
        Map<String, Object> claims = new HashMap<>();
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(request.getUserName())
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setCreation(createdDate);
        loginResponse.setExpiration(expirationDate);
        loginResponse.setToken(token);

        return loginResponse;
    }

    private Date calculateExpirationDate(Date createdDate) {
        long expiration = expirationDuration;
        return new Date(createdDate.getTime() + expiration*1000);
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
//        log.info("claims: {}", claims.toString());
        return claimsResolver.apply(claims);
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parse(token);
            return true;

        } catch (MalformedJwtException e) {
            log.error("Invalid JWT Token", e);
            throw new AuthenticationException("Invalid JWT Token");
        }catch(ExpiredJwtException e){
            log.error("JWT Token is Expired", e);
            throw new AuthenticationException("JWT Token has expired");
        }catch(UnsupportedJwtException e){
            log.error("Unsupported JWT", e);
            throw new AuthenticationException("Unsupported JWT Token");
        }catch(IllegalArgumentException e){
            log.error("JWT Payload is Empty", e);
            throw new AuthenticationException("Unable to get JWT Token");
        }catch (Exception ex){
            log.error("Exception occurred while validating JWT Token", ex);
            throw new AuthenticationException("Exception occurred while validating JWT Token");
        }
    }

}
