package org.authorization.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/admin")
public class AdminController {
  @GetMapping
  public String adminData() {
    return "Admin-specific data";
  }
}
