package org.example.bff.config;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ModelAttributesAdvice {

    @ModelAttribute("isAuthenticated")
    public Mono<Boolean> isAuthenticated() {
        return ReactiveSecurityContextHolder
                .getContext()
                .map(SecurityContext::getAuthentication)
                .map(auth -> auth != null && auth.isAuthenticated())
                .defaultIfEmpty(false);
    }

    @ModelAttribute("roles")
    public Mono<Map<String, Boolean>> roles() {
        return ReactiveSecurityContextHolder
                .getContext()
                .map(SecurityContext::getAuthentication)
                .map(auth -> {
                    Map<String, Boolean> roles = new HashMap<>();
                    roles.put("isAdmin", auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN")));
                    roles.put("isLeader", auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_LEADER")));
                    roles.put("isUser", auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_USER")));
                    roles.put("isGuest", auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_GUEST")));
                    return roles;
                }).defaultIfEmpty(Map.of("isAdmin", false,
                        "isLeader", false,
                        "isUser", false,
                        "isGuest", false));
    }

}
