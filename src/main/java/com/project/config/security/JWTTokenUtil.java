package com.project.config.security;

import com.project.DTO.LoginRequest;
import com.project.DTO.LoginResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
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
        final Claims claims = getAllClaimsFromToken(token);
//        log.info("claims: {}", claims.toString());
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        JwtUserDetails user = (JwtUserDetails) userDetails;
        final String username = getUsernameFromToken(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(clock.now());
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
}
