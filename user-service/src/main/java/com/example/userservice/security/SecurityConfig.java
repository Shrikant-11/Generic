package com.example.userservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

  private final JwtFilter jwtFilter;
  private final ApiKeyFilter apiKeyFilter;

  public SecurityConfig(JwtFilter jwtFilter, ApiKeyFilter apiKeyFilter) {
    this.jwtFilter = jwtFilter;
    this.apiKeyFilter = apiKeyFilter;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(authz -> authz
          .requestMatchers("/api/auth/**", "/api/public/**",
              "/error", "/v3/api-docs/**",
              "/swagger-ui/**",
              "/swagger-ui.html",
              "/user/register").permitAll()
          .requestMatchers("/api/admin/**").hasRole("ADMIN")
          .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
          .requestMatchers("/api/client/**").hasAuthority("CLIENT")
          .anyRequest().authenticated()
      )
      .addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class)
      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
