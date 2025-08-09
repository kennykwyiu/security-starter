package com.kenny.uaa.rest;

import com.kenny.uaa.domain.Auth;
import com.kenny.uaa.domain.User;
import com.kenny.uaa.domain.dto.LoginDto;
import com.kenny.uaa.domain.dto.UserDto;
import com.kenny.uaa.exception.DuplicateProblem;
import com.kenny.uaa.service.UserCacheService;
import com.kenny.uaa.service.UserService;
import com.kenny.uaa.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/authorize")
public class AuthorizeResource {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserCacheService userCacheService;

    @PostMapping("/register")
    public UserDto register(@Valid @RequestBody UserDto userDto) {
        if (userService.isUsernameExist(userDto.getUsername())) {
            throw new DuplicateProblem("Username is already in use");
        }

        if (userService.isEmailExist(userDto.getEmail())) {
            throw new DuplicateProblem("Email is already in use");
        }

        if (userService.isMobileExist(userDto.getMobile())) {
            throw new DuplicateProblem("Mobile number is already in use");
        }
        val user = User.builder()
                .username(userDto.getUsername())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .mobile(userDto.getMobile())
                .password(userDto.getPassword())
                .build();
        User savedUser = userService.register(user);
        return UserDto.builder()
                .username(savedUser.getUsername())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .mobile(savedUser.getMobile())
                .build();
    }

    @PostMapping("/token")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) throws AccessDeniedException {
        return userService.findOptionalByUsernameAndPassword(loginDto.getUsername(), loginDto.getPassword())
                .map(user -> {
                    // Step 1: Upgrade password encoding if needed

                    // Step 2: Check if user is enabled

                    // Step 3: Check if user account is non-locked

                    // Step 4: Check if user account is non-expired

                    // Step 5: If user does not use MFA, return login result
                    if (!user.isUsingMfa()) {
                        return ResponseEntity.ok().body(userService.login(loginDto.getUsername(), loginDto.getPassword()));
                    }

                    // Step 6: If user uses MFA, cache user and return 401 with MFA challenge
                    String mfaId = userCacheService.cacheUser(user);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .header("x-Authenticate", "mfa", "realm=" + mfaId)
                            .build();
                })
                .orElseThrow(() -> new BadCredentialsException("username or password invalid"));

    }

    @PostMapping("/token/refresh")
    public Auth refreshToken(@RequestHeader(name = "Authorization") String authorization,
                             @RequestParam String refreshToken) throws Exception {
        String PREFIX = "Bearer ";
        String accessToken = authorization.replace(PREFIX, "");
        if (jwtUtil.validateRefreshToken(refreshToken) && jwtUtil.validateAccessTokenWithoutExpiration(accessToken)) {
            return new Auth(jwtUtil.createAccessTokenWithRefreshToken(refreshToken), refreshToken);
        }
        throw new AccessDeniedException("Access Denied");
    }
}
