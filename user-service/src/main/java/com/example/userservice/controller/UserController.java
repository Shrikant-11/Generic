package com.example.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("/info")
    public ResponseEntity<String> info() {
        return ResponseEntity.ok("User info");
    }
}