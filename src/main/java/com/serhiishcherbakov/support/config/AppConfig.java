package com.serhiishcherbakov.support.config;

import com.serhiishcherbakov.support.security.AuthExceptionHandler;
import com.serhiishcherbakov.support.security.AuthorizationFilter;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final AuthorizationFilter authorizationFilter;
    private final AuthExceptionHandler authExceptionHandler;

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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/operator/**").hasRole("OPERATOR")
                        .requestMatchers("/api/user/**").hasRole("USER")
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authExceptionHandler)
                        .accessDeniedHandler(authExceptionHandler)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .passwordManagement(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .build();
    }
}
