package org.example.userservice.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.userservice.entity.Role;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class JwtTokenService {

    private final Key privateKey;

    public JwtTokenService(Key privateKey) {
        this.privateKey = privateKey;
    }

    public String generateToken(Long userId, List<String> roles) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(userId.toString())
                .addClaims(Map.of("roles", roles))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now+1_800_000))
                .signWith(privateKey, SignatureAlgorithm.ES256)
                .compact();

    }
}
