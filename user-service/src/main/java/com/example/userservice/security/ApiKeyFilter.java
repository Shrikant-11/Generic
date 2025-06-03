package com.example.userservice.security;

import com.example.userservice.domain.ApiKey;
import com.example.userservice.repository.ApiKeyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyFilter(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Only process API key for /api/client/** paths
        if (!request.getServletPath().startsWith("/api/client/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = extractApiKey(request);
        if (apiKey != null && apiKey.contains(".")) {
            String[] parts = apiKey.split("\\.", 2);
            String prefix = parts[0];
            String secret = parts[1];
        
            Optional<ApiKey> foundKeyOpt = apiKeyRepository.findByPrefixAndActiveTrue(prefix);
        
            if (foundKeyOpt.isPresent()) {
                ApiKey foundKey = foundKeyOpt.get();
                String storedHash = foundKey.getKeyValue().split("\\.", 2)[1]; // Get the hashed part after prefix
        
                if (BCrypt.checkpw(secret, storedHash) && isValidApiKey(foundKey)) {
                    var authorities = List.of(new SimpleGrantedAuthority("CLIENT"));
                    var authentication = new UsernamePasswordAuthenticationToken(
                        foundKey.getName(), null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractApiKey(HttpServletRequest request) {
        String apiKey = request.getHeader("X-API-KEY");
        if (apiKey == null) {
            // Also check Authorization header with "ApiKey " prefix
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("ApiKey ")) {
                apiKey = authHeader.substring(7);
            }
        }
        return apiKey;
    }

    private boolean isValidApiKey(ApiKey apiKey) {
        if (!apiKey.isActive()) {
            return false;
        }
        
        if (apiKey.getExpiresAt() != null) {
            return apiKey.getExpiresAt().isAfter(LocalDateTime.now());
        }
        
        return true;
    }
} 