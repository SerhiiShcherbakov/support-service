package com.serhiishcherbakov.support.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("support-service").version("v1"))
                .components(new Components()
                        .addSecuritySchemes(
                                "user-token",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .name("Authorization")
                                        .in(SecurityScheme.In.HEADER)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Example: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0LTEiLCJuYW1lIjoiVGVzdCBVc2VyIiwicGljdHVyZSI6Ii9sb2dvcy90ZXN0LXVzZXItY29weS5wbmciLCJyb2xlIjoiVVNFUiIsInVwZGF0ZWRBdCI6IjIwMjUtMDYtMTJUMTQ6NDc6NTMuNjg3WiIsImlhdCI6MTc2MDU0MjU1NiwiZXhwIjoxNzYwNTQ0MzU2fQ.3vyE7RUDiNvb1HPorNh3z3z-xHslKjdl7q6gqy6AYg4")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("user-token"));
    }
}
