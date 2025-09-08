package org.example.bff.security;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.example.bff.service.JwtService;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthManager implements ReactiveAuthenticationManager {

    private final JwtService jwtService;

    public Mono<Authentication> authenticate (Authentication authentication) {
        String token = (String) authentication.getCredentials();

        return Mono.fromCallable(() -> {
            Claims claims = jwtService.validateToken(token);
            String userId = claims.getSubject();
            Object o = claims.get("roles");
            AuthUser authUser = new AuthUser(userId, (String) o);
            return new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());
        });
    }
}
