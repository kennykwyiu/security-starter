package com.kenny.uaa.rest;

import com.kenny.uaa.domain.Auth;
import com.kenny.uaa.domain.dto.LoginDto;
import com.kenny.uaa.domain.dto.UserDto;
import com.kenny.uaa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/authorize")
public class AuthorizeResource {
    private final UserService userService;

    @PostMapping("/register")
    public UserDto register(@Valid @RequestBody UserDto userDto) {
        return userDto;
    }

    @PostMapping("/token")
    public Auth login(@Valid @RequestBody LoginDto loginDto) throws Exception {
        return userService.login(loginDto.getUsername(), loginDto.getPassword());
    }
}
