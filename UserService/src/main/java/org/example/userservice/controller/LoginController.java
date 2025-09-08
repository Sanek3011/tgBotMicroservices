package org.example.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.IdRequestDto;
import org.example.userservice.security.JwtResponse;
import org.example.userservice.service.LoginService;
import org.example.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/login")
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<String> getOneTimeToken(@RequestBody IdRequestDto requestDto) {
        String token = loginService.getOrSaveToken(requestDto.getTgId());
        return ResponseEntity.ofNullable(token);
    }

    @PostMapping("/jwt")
    public ResponseEntity<JwtResponse> getJwtToken(@RequestBody Map<String, String> map) {
        String token = map.get("token");
        JwtResponse jwtResponse = loginService.generateJwt(token);
        return ResponseEntity.ofNullable(jwtResponse);
    }

    @PostMapping("/jwt/refresh")
    public ResponseEntity<JwtResponse> refreshToken(@RequestBody Map<String, String> map) {
        String token = map.get("refreshToken");
        JwtResponse jwtResponse = loginService.refreshToken(token);
        if (jwtResponse == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(jwtResponse);
    }

}
