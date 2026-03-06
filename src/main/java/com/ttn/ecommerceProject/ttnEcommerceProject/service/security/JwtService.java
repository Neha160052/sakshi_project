package com.ttn.ecommerceProject.ttnEcommerceProject.service.security;


import com.ttn.ecommerceProject.ttnEcommerceProject.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Slf4j
@Service
public class JwtService {


    @Value("${jwt.secret}")
    private String secret;
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String email, Long expiration){
        return Jwts.builder()
                .setSubject(email)

                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token){
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try{
            String username = extractUsername(token);
            return true;
        }catch (Exception ex){
            log.error("Exception in validation :", ex);
            return false;
        }
    }

    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    private boolean isExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    public boolean isTokenValid(String token,String username){
        return extractUsername(token).equals(username) && !isExpired(token);
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }






}
