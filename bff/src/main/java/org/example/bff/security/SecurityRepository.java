package org.example.bff.security;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.example.bff.service.outputService.LoginOutputService;
import org.example.bff.service.TokenCookieService;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SecurityRepository implements ServerSecurityContextRepository {

    private final JwtAuthManager authManager;
    private final LoginOutputService outputService;
    private final TokenCookieService tokenCookieService;

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        return Mono.justOrEmpty(extractToken(exchange))
                .flatMap(token -> {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(token, token);
                    return authManager.authenticate(auth)
                            .map(authoriz -> (SecurityContext) new SecurityContextImpl(authoriz));
                }).onErrorResume(e -> {
                    if (e instanceof ExpiredJwtException) {
                        HttpCookie refreshToken = exchange.getRequest().getCookies().getFirst("refresh_token");
                        if (refreshToken == null) {
                            return Mono.empty();
                        }
                        String refreshTokenValue = refreshToken.getValue();
                        return outputService.refreshToken(refreshTokenValue)
                                .flatMap(jwt -> {
                                    tokenCookieService.addAuthCookies(exchange, jwt);
                                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(jwt.getAccessToken(), jwt.getAccessToken());
                                    return authManager.authenticate(auth)
                                            .map(authoriz -> (SecurityContext) new SecurityContextImpl(authoriz));
                                });
                    }
                    return Mono.empty();
                });
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    private String extractToken(ServerWebExchange exchange) {
        String authorization = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        HttpCookie accessToken = exchange.getRequest().getCookies().getFirst("access_token");
        if (accessToken != null) {
            return accessToken.getValue();
        }
        return null;
    }
}
