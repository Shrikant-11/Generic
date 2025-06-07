package com.example.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.userservice.domain.User;
import com.example.userservice.dto.UserRequestDTO;
import com.example.userservice.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public ResponseEntity<String> info() {
        return ResponseEntity.ok("User info");
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        User user = userService.registerUser(userRequestDTO);
        return ResponseEntity.ok(user);
    }
}