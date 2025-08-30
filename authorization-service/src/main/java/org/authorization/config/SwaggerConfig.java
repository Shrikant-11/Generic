package org.authorization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

        private static final String SECURITY_SCHEME_NAME = "BearerAuth";

        @Bean
        public OpenAPI customOpenAPI() {
                Server apiGatewayServer = new Server();
                apiGatewayServer.setUrl("http://localhost:8080/authorization-service"); // API Gateway URL

                Server selfServer = new Server();
                selfServer.setUrl("http://localhost:9999"); // Microservice's own URL

                return new OpenAPI()
                                .addServersItem(apiGatewayServer)
                                .addServersItem(selfServer)
                                .info(new Info()
                                                .title("Authorization Service APIs")
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
