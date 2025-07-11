package com.kenny.uaa.rest;

import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserResource {

    @GetMapping("/greeting")
    public String greeting() {
        return "Hello World!";
    }

    @PostMapping("/greeting")
    public String makeGreeting(@RequestParam String name, @RequestBody Profile profile) {
        return "Hello " + name + "!\n" + profile.gender;
    }

    @PutMapping("/greeting/{name}")
    public String putGreeting(@PathVariable String name) {
        return "Hello " + name + "!";
    }

    @GetMapping("/principal")
    public Authentication getPrincipal() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Data
    static class Profile {
        private String gender;
        private String idNo;
    }
}
