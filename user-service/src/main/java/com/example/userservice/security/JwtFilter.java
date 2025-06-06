package com.example.userservice.security;

import com.example.userservice.service.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;
  private final AuthenticationService authenticationService;

  public JwtFilter(JwtUtil jwtUtil, AuthenticationService authenticationService) {
    this.jwtUtil = jwtUtil;
    this.authenticationService = authenticationService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {

    String token = extractToken(request);
    if (token != null && jwtUtil.validateToken(token)) {
      String username = jwtUtil.getUsername(token);
      String role = jwtUtil.getClaim(token, "role", String.class);

      if ("CLIENT".equals(role)) {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } else {
        UserDetails userDetails = authenticationService.loadUserByUsername(username);
      //  if (userDetails instanceof CustomUserDetails customUserDetails) {
       /*   String education = customUserDetails.getEducation();
          String mobile = customUserDetails.getMobileNumber();
          System.out.println("Education: " + education);
        }*/
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }

    filterChain.doFilter(request, response);
  }

  private String extractToken(HttpServletRequest request) {
    String token = request.getHeader("Authorization");
    if (token != null && token.startsWith("Bearer ")) {
      return token.substring(7);
    }
    return null;
  }
}
