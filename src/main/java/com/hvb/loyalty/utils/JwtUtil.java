package com.hvb.loyalty.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generarToken(String correo, Long id) {
        Date ahora = new Date();
        Date expira = new Date(ahora.getTime() + expiration);
        return Jwts.builder()
                .subject(correo)
                .claim("id", id)
                .issuedAt(ahora)
                .expiration(expira)
                .signWith(getKey())
                .compact();
    }

    public String extraerCorreo(String token) {
        return parse(token).getSubject();
    }

    public boolean validarToken(String token) {
        try {
            parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}