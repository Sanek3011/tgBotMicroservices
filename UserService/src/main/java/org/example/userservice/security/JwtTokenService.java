package org.example.userservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.userservice.entity.User;
import org.example.userservice.service.JtiService;
import org.springframework.stereotype.Service;


import java.security.Key;
import java.time.Instant;
import java.util.Date;

import java.util.Map;
import java.util.UUID;

@Service
public class JwtTokenService {

    private final Key privateKey;
    private final Key refreshKey;
    private final JtiService jtiService;

    public JwtTokenService(Key privateKey, Key refreshKey, JtiService jtiService) {
        this.privateKey = privateKey;
        this.refreshKey = refreshKey;
        this.jtiService = jtiService;
    }

    public String generateAccessToken(User user) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .addClaims(Map.of("roles", user.getRole()))
                .addClaims(Map.of("score", user.getScore()))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now+600_000))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();

    }

    public String generateRefreshToken(User user) {
        long now = System.currentTimeMillis();
        String jti = UUID.randomUUID().toString();
        Instant expiresAt = Instant.ofEpochMilli(now + 30L * 24 * 60 * 60 * 1000);
        jtiService.removeOldToken(user);
        jtiService.save(jti, user, expiresAt);
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setId(jti)
                .setExpiration(Date.from(expiresAt))
                .signWith(refreshKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public boolean validateAccess(String accessToken) {
        return validateToken(accessToken, privateKey);
    }

    public boolean validateRefresh(String refreshToken) {

        return validateToken(refreshToken, refreshKey);
    }

    private boolean validateToken(String token, Key secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getAccessClaims(String token) {
        return getClaims(token, privateKey);
    }

    public Claims getRefreshClaims(String token) {
        return getClaims(token, refreshKey);
    }

    private Claims getClaims(String token, Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
