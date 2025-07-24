package com.kenny.uaa.service;

import com.kenny.uaa.config.Constants;
import com.kenny.uaa.domain.Auth;
import com.kenny.uaa.domain.User;
import com.kenny.uaa.repository.RoleRepo;
import com.kenny.uaa.repository.UserRepo;
import com.kenny.uaa.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
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

    public boolean isUsernameExist(String username) {
        return userRepo.countByUsername(username) > 0;
    }

    public boolean isEmailExist(String email) {
        return userRepo.countByEmail(email) > 0;
    }

    public boolean isMobileExist(String mobile) {
        return userRepo.countByMobile(mobile) > 0;
    }

    public User register(User user) {
        return roleRepo.findOptionalByAuthority(Constants.ROLE_USER)
                .map(role -> {
                    User userToSave = user.withAuthorities(Set.of(role))
                            .withPassword(passwordEncoder.encode(user.getPassword()));
                    return userToSave;
                })
                .orElseThrow();
    }
}
