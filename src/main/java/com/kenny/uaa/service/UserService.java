package com.kenny.uaa.service;

import com.kenny.uaa.domain.Auth;
import com.kenny.uaa.repository.UserRepo;
import com.kenny.uaa.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Auth login(String username, String password) throws AuthenticationException {
        return userRepo.findOptionalByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> new Auth(
                                jwtUtil.createAccessToken(user),
                                jwtUtil.createRefreshToken(user)
                        )
                )
                .orElseThrow(() -> new BadCredentialsException("Username or password is incorrect"));
    }
}
