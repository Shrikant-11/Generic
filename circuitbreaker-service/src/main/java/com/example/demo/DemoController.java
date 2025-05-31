package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    private final BackendService backendService;

    public DemoController(BackendService backendService) {
        this.backendService = backendService;
    }

    @GetMapping("/api/test")
    public String testEndpoint() {
        return backendService.fetchData();
    }
}
