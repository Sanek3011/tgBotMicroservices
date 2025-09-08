package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import org.example.userservice.entity.Role;
import org.example.userservice.entity.User;
import org.example.userservice.security.JwtResponse;
import org.example.userservice.security.JwtTokenService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final JtiService jtiService;


    public String getOrSaveToken(Long tgId) {
        try {
            User user = userService.findUserByTgId(tgId).orElseThrow();
            if (user.getRole().equals(Role.GUEST)) {
                throw new RuntimeException();
            }
            return userService.generateOneTimeToken(user);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public JwtResponse generateJwt(String token){
        try {
            User user = userService.findByToken(token).orElseThrow();
            user.setExpiresAt(LocalDateTime.now());
            userService.saveUser(user);
            return new JwtResponse(jwtTokenService.generateAccessToken(user), jwtTokenService.generateRefreshToken(user));
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public JwtResponse refreshToken(String refreshToken) {
        if (jwtTokenService.validateRefresh(refreshToken)) {
            String jti = jwtTokenService.getRefreshClaims(refreshToken).getId();
            if (!jtiService.validateJti(jti)) {
                return null;
            }
            String userId = jwtTokenService.getRefreshClaims(refreshToken).getSubject();
            User user = userService.findByUserId(Long.parseLong(userId));
            return new JwtResponse(jwtTokenService.generateAccessToken(user), jwtTokenService.generateRefreshToken(user));
        }
        return null;
    }
}
