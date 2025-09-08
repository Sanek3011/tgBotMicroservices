package org.example.bff.security;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JWTResponse {
    private final String type = "Bearer";
    private String accessToken;
    private String refreshToken;
    private Long expiresAt;
}
