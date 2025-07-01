package com.kenny.uaa.rest;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserResource {

    @GetMapping("/greeting")
    public String greeting() {
        return "Hello World!";
    }

    @PostMapping("/greeting")
    public String makeGreeting(@RequestParam String name) {
        return "Hello " + name + "!";
    }
}
