package com.example.userservice.aspect;

import com.example.userservice.domain.ApiKey;
import com.example.userservice.domain.ApiUsage;
import com.example.userservice.repository.ApiKeyRepository;
import com.example.userservice.repository.ApiUsageRepository;
import com.example.userservice.utils.extractApiKeyUtil;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.time.LocalDateTime;
import java.util.Optional;


@Aspect
@Component
public class ApiUsageTracker {

    private final ApiUsageRepository apiUsageRepository;
    private final ApiKeyRepository apiKeyRepository;

    public ApiUsageTracker(ApiUsageRepository apiUsageRepository, ApiKeyRepository apiKeyRepository) {
        this.apiUsageRepository = apiUsageRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    @Around("@within(org.springframework.web.bind.annotation.RestController) && " +
            "execution(* com.example.userservice.controller.ClientApiController.*(..))")
    public Object trackApiUsage(ProceedingJoinPoint joinPoint) throws Throwable {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return joinPoint.proceed();
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String apiKeyValue = extractApiKeyUtil.extractApiKey(request);
        
        if (apiKeyValue == null || !apiKeyValue.contains(".")) {
            return joinPoint.proceed();
        }

        String prefix = apiKeyValue.split("\\.", 2)[0];
        Optional<ApiKey> foundKey = apiKeyRepository.findByPrefixAndActiveTrue(prefix);
        
        if (foundKey.isEmpty()) {
            return joinPoint.proceed();
        }

        ApiKey apiKey = foundKey.get();
        long startTime = System.currentTimeMillis();
        Object result = null;
        Integer responseStatus = 200;

        try {
            result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            responseStatus = 500;
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            
            ApiUsage usage = ApiUsage.builder()
                    .apiKeyId(apiKey)
                    .endpoint(request.getRequestURI())
                    .method(request.getMethod())
                    .ipAddress(request.getRemoteAddr())
                    .responseStatus(responseStatus)
                    .responseTimeMs(endTime - startTime)
                    .timestamp(LocalDateTime.now())
                    .build();
            
            apiUsageRepository.save(usage);
        }
    }
} 