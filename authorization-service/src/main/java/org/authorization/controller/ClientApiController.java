package org.authorization.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client")
public class ClientApiController {

    @GetMapping("/data")
    public String getClientData(Authentication auth) {
        return "Client-only access granted to: " + auth.getName();
    }
}
