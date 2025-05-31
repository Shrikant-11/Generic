package org.authorization.security;



import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.authorization.domain.CustomUserDetails;
import org.authorization.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
  private final UserServiceImpl userDetailsService;

  @Autowired
  public JwtFilter(JwtUtil jwtUtil, UserServiceImpl userDetailsService) {
    this.jwtUtil = jwtUtil;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {

    String token = extractToken(request);
    if (token != null && jwtUtil.validateToken(token)) {
      String subject = jwtUtil.getUsername(token);
      String role = jwtUtil.getClaim(token, "role", String.class);

      if ("CLIENT".equals(role)) {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(subject, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } else {
        UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
        if (userDetails instanceof CustomUserDetails customUserDetails) {
          String email = customUserDetails.getEmail();
          String address = customUserDetails.getAddress();
          String education = customUserDetails.getEducation();
          System.out.println("Email: " + email + " Address: " + address);
          // Use these fields as needed (e.g. logging, enrichment, etc.)
        }
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
