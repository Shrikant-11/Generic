package com.example.userservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

        private static final String SECURITY_SCHEME_NAME = "BearerAuth";

        @Bean
        public OpenAPI customOpenAPI() {
                Server apiGatewayServer = new Server();
                apiGatewayServer.setUrl("http://localhost:8765/user-service"); // API Gateway URL

                Server selfServer = new Server();
                selfServer.setUrl("http://localhost:8082"); // Microservice's own URL

                return new OpenAPI()
                                .addServersItem(apiGatewayServer)
                                .addServersItem(selfServer)
                                .info(new Info()
                                                .title("User Service APIs")
                                                .version("v1"))
                                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                                .components(new Components()
                                                .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                                                new SecurityScheme()
                                                                                .name(SECURITY_SCHEME_NAME)
                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                .scheme("bearer")
                                                                                .bearerFormat("JWT")));
        }
}
