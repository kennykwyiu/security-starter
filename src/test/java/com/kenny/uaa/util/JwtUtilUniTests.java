package com.kenny.uaa.util;

import com.kenny.uaa.domain.Role;
import com.kenny.uaa.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class JwtUtilUniTests {
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    public void giveUserDetails_thenCreateTokenSuccess() {
        String username = "user";
        Set<Role> authorities = Set.of(
                Role.builder()
                        .authority("ROLE_USER")
                        .build(),
                Role.builder()
                        .authority("ROLE_ADMIN")
                        .build()
        );
        User user = User.builder()
                .username(username)
                .authorities(authorities)
                .build();

        String token = jwtUtil.createAccessToken(user);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtUtil.key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        assertEquals(username, claims.getSubject(), "username is same as user");

    }

}
