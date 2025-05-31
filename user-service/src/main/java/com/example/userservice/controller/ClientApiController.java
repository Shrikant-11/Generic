package com.example.userservice.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
public class ClientApiController {

    @GetMapping("/data")
    public String getClientData(Authentication auth) {
        return "Client-only access granted to: " + auth.getName();
    }
}
