package com.bbc.app.service.impl;

import com.bbc.app.model.User;
import com.bbc.app.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {
    private final String SECRET_KEY =
            "d7fdf7281aa225176d9a87d513fcadde19839433c59f268cee98f3f9dbec520c";

    @Override
    public String extractCustomUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    @Override
    @Transactional(readOnly = true) // Ensure this method runs in a transaction
    public boolean isValid(String token, UserDetails user) {
        String customUserId = extractCustomUserId(token);
        boolean isTokenExpired = isTokenExpired(token);

        // Initialize the lazy-loaded properties only if user is an instance of User
        if (user instanceof User castedUser) {

            // Ensure that lazy-loaded properties are initialized
            if (castedUser.getEmployee() != null) {
                Hibernate.initialize(castedUser.getEmployee());
            }
            if (castedUser.getCustomer() != null) {
                Hibernate.initialize(castedUser.getCustomer());
            }
        }

        return customUserId.equals(user.getUsername()) && !isTokenExpired;
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public String generateToken(User user) {
        return Jwts
                .builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .signWith(getSigninKey())
                .compact();
    }

    private SecretKey getSigninKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}