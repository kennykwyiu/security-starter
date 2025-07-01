package com.kenny.uaa.rest;

import lombok.Data;
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

    @Data
    static class Profile {
        private String gender;
        private String idNo;
    }
}
