package com.kenny.uaa.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    public static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    public static final Key refreshKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String createAccessToken(UserDetails userDetails) {
        return createJwtToken(userDetails, key);
    }

    public String createRefreshToken(UserDetails userDetails) {
        return createJwtToken(userDetails, refreshKey);
    }

    public String createJwtToken(UserDetails userDetails, Key key) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setId("kenny")
                .claim("authorities", userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority
                        ).collect(Collectors.toList()))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 60_000))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}
