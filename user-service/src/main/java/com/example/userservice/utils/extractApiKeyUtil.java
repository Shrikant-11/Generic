package com.example.userservice.utils;

import jakarta.servlet.http.HttpServletRequest;

public class extractApiKeyUtil {
    public static String extractApiKey(HttpServletRequest request) {
        String apiKey = request.getHeader("X-API-KEY");
        if (apiKey == null) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("ApiKey ")) {
                apiKey = authHeader.substring(7);
            }
        }
        return apiKey;
    }
}
