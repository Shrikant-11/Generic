package com.example.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
public class ClientApiController {

    @GetMapping("/data")
    public ResponseEntity<String> getClientData(Authentication auth) {
        String message = "Client-only access granted to: " + auth.getName();
        return ResponseEntity.ok(message);
    }
}
