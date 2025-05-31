package org.authorization.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
  @PostMapping("/data")
  public String adminData() {
    return "Admin-specific data";
  }
}
