package com.serhiishcherbakov.support.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.serhiishcherbakov.support.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Instant;
import java.time.format.DateTimeParseException;

@Slf4j
@Component
public class UserInterceptor implements HandlerInterceptor {
    public static final String USER_DETAILS_ATTRIBUTE = "userDetails";
    private static final String USER_AUTHORIZATION_HEADER = "Authorization";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        var jwtToken = extractJwtToken(request);
        if (StringUtils.isBlank(jwtToken)) {
            throw new UnauthorizedException();
        }
        request.setAttribute(USER_DETAILS_ATTRIBUTE, extractUserDetails(jwtToken));
        return true;
    }

    private String extractJwtToken(HttpServletRequest request) {
        var authHeader = request.getHeader(USER_AUTHORIZATION_HEADER);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.replaceFirst("Bearer ", "");
    }

    private UserDetails extractUserDetails(String jwtToken) {
        try {
            var jwt = JWT.decode(jwtToken);
            validateTokenClaims(jwt);

            return new UserDetails(
                    jwt.getClaim("sub").asString(),
                    jwt.getClaim("name").asString(),
                    jwt.getClaim("picture").asString(),
                    jwt.getClaim("role").asString(),
                    Instant.parse(jwt.getClaim("updatedAt").asString())
            );
        } catch (JWTDecodeException | DateTimeParseException e) {
            log.error("Failed to decode JWT token: {}", e.getMessage());
            throw new UnauthorizedException();
        }
    }

    private void validateTokenClaims(DecodedJWT jwt) {
        validateTokenClaim(jwt, "sub");
        validateTokenClaim(jwt, "name");
        validateTokenClaim(jwt, "role");
        validateTokenClaim(jwt, "updatedAt");
    }

    private void validateTokenClaim(DecodedJWT jwt, String claimName) {
        if (jwt.getClaim(claimName).isMissing()) {
            throw new UnauthorizedException("JWT token is missing required claim: " + claimName);
        }
    }
}
