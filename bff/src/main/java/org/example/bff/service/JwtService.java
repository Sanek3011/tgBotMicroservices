package org.example.bff.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.example.bff.Util.KeyReader;
import org.example.bff.security.AuthUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.PublicKey;

@Service
public class JwtService {

    private final PublicKey publicKey;

    public JwtService(KeyReader keyReader) {
        this.publicKey = keyReader.readPublicKey("classpath:/keys/public-key.pem");
    }

    public Claims validateToken(String token) {
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token);
        return claimsJws.getBody();
    }
}
