package org.authorization.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Authorization Service API",
        version = "1.0",
        description = "API for managing authentication and authorization",
        contact = @Contact(name = "Support Team", email = "support@example.com")
    ),
    servers = {
        @Server(url = "http://localhost:9999", description = "Local Development Server")
    }
)
@SecuritySchemes({
    @SecurityScheme(
        name = "bearerAuth",
        description = "JWT token authentication",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
    ),
    @SecurityScheme(
        name = "apiKey",
        description = "API Key authentication",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "X-API-KEY"
    )
})
public class OpenApiConfig {
} 