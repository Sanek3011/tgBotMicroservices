package org.example.bff.service;

import lombok.RequiredArgsConstructor;
import org.example.bff.security.JWTResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class TokenCookieService {

    public void addAuthCookies(ServerWebExchange exchange, JWTResponse jwt) {
        ResponseCookie accessToken = ResponseCookie.from("access_token", jwt.getAccessToken())
                .httpOnly(true)
                .maxAge(Duration.of(15, ChronoUnit.MINUTES))
                .path("/")
                .sameSite("Lax")
                .build();
        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", jwt.getRefreshToken())
                .httpOnly(true)
                .maxAge(Duration.ofDays(10))
                .path("/")
                .sameSite("Lax")
                .build();
        exchange.getResponse().addCookie(accessToken);
        exchange.getResponse().addCookie(refreshCookie);
    }
}
