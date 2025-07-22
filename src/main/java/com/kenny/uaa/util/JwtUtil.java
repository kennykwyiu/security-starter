package com.kenny.uaa.util;

import com.kenny.uaa.config.AppProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class JwtUtil {
    public static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    public static final Key refreshKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    private final AppProperties appProperties;

    public String createAccessToken(UserDetails userDetails) {
        return createJwtToken(userDetails,
                appProperties.getJwt().getAccessTokenExpireTime(),
                key);
    }

    public String createRefreshToken(UserDetails userDetails) {
        return createJwtToken(userDetails,
                appProperties.getJwt().getRefreshTokenExpireTime(),
                refreshKey);
    }

    /*
    The logic
    `
    if (e instanceof ExpiredJwtException) {
        return !isExpiredInvalid;
    }
    `
    exists to:

    1. Provide configurable behavior for expired tokens through the `isExpiredInvalid` parameter
    2. Allow expired tokens to be accepted in special cases (when isExpiredInvalid=false)
    3. Maintain strict validation for all other types of invalid tokens (MalformedJwtException, etc.)
    4. Support use cases like token refresh flows where you might want to validate an expired token before issuing a new one

    This is particularly useful for:

    - Grace periods where expired tokens might still be accepted temporarily
    - Refresh token workflows where you need to validate an expired access token
    - Special API endpoints that might accept expired tokens for limited functionality

     */
    public boolean validateAccessTokenWithoutExpiration(String token) {
        return validateToken(token, key, false);
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, key, true);
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, refreshKey, true);
    }

    public boolean validateToken(String token, Key key, boolean isExpiredInvalid) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parse(token);
            return true;
        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | SignatureException | IllegalArgumentException e) {
            if (e instanceof ExpiredJwtException) {
                return !isExpiredInvalid;
            }
            return false;
        }
    }

    public String createJwtToken(UserDetails userDetails, long timeToExpire, Key key) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setId("kenny")
                .claim("authorities", userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority
                        ).collect(Collectors.toList()))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + timeToExpire))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}
