package com.kenny.uaa.security.jwt;

import com.kenny.uaa.config.AppProperties;
import com.kenny.uaa.util.CollectionUtil;
import com.kenny.uaa.util.JwtUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final AppProperties appProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (checkJwtToken(httpServletRequest)) {
            validateToken(httpServletRequest)
                    .filter(claims -> claims.get("authorities") != null)
                    .ifPresentOrElse(claims -> {
                            // ifPresent
                                setupSpringAuthentication(claims);
                            },
                            () -> {
                            // ifNull
                                SecurityContextHolder.clearContext();
                            });
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private static void setupSpringAuthentication(Claims claims) {
        List<?> rawList = CollectionUtil.convertObjectToList(claims.get("authorities"));
        List<SimpleGrantedAuthority> authorities = rawList.stream()
                .map(raw -> String.valueOf(raw))
                .map(authority -> new SimpleGrantedAuthority(authority))
                .collect(Collectors.toList());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private Optional<Claims> validateToken(HttpServletRequest request) {
        String jwtToken = request.getHeader(appProperties.getJwt().getHeader()).replace(appProperties.getJwt().getPrefix(), "");
        try {
            return Optional.of(Jwts.parserBuilder()
                    .setSigningKey(JwtUtil.key).build().parseClaimsJws(jwtToken).getBody());
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private boolean checkJwtToken(HttpServletRequest request) {
        String authenticationHeader = request.getHeader(appProperties.getJwt().getHeader());
        return authenticationHeader != null
                && authenticationHeader.startsWith(appProperties.getJwt().getPrefix());
    }
}
