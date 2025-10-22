package com.serhiishcherbakov.support.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {
    private final List<String> requiredTokenClaims = List.of("sub", "name", "role", "updatedAt");

    public boolean validateToken(String jwtToken) {
        return StringUtils.isNotBlank(jwtToken) && containsAllClaims(jwtToken);
    }

    private boolean containsAllClaims(String jwtToken) {
        try {
            var jwt = JWT.decode(jwtToken);
            return requiredTokenClaims.stream()
                    .noneMatch(requiredClaim -> jwt.getClaim(requiredClaim).isMissing());
        } catch (JWTDecodeException | DateTimeParseException e) {
            log.error("Failed to decode JWT token: {}", e.getMessage());
            return false;
        }
    }

    public UserDetailsDto parseToken(String jwtToken) {
        try {
            var jwt = JWT.decode(jwtToken);
            return new UserDetailsDto(
                    jwt.getClaim("sub").asString(),
                    jwt.getClaim("name").asString(),
                    jwt.getClaim("picture").asString(),
                    jwt.getClaim("role").asString(),
                    Instant.parse(jwt.getClaim("updatedAt").asString())
            );
        } catch (JWTDecodeException | DateTimeParseException e) {
            log.error("Failed to decode JWT token: {}", e.getMessage());
            return null;
        }
    }
}
