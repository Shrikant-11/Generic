package org.authorization.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public")
public class PublicController {
  @GetMapping("/hello")
  public String hello() {
    return "Hello, World!";
  }
}
