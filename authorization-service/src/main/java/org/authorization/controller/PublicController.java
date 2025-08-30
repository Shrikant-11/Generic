package org.authorization.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/api/v1")
public class PublicController {
  @GetMapping("/hello")
  public String hello() {
    return "Hello, World!";
  }
}
