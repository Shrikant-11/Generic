package org.authorization.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
  @PostMapping("/data")
  public String userData() {
    return "User-specific data";
  }
}
