package org.authorization.controller;


import org.authorization.domain.Client;
import org.authorization.domain.CustomUserDetails;
import org.authorization.domain.User;
import org.authorization.dto.AuthRequestDTO;
import org.authorization.dto.OAuthRequestDTO;
import org.authorization.dto.RefreshTokenRequestDTO;
import org.authorization.repository.UserRepository;
import org.authorization.security.JwtUtil;
import org.authorization.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;
  private final ClientService clientService;
  private final UserRepository userRepository;

  @Autowired
  public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, ClientService clientService, UserRepository userRepository) {
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
    this.clientService = clientService;
      this.userRepository = userRepository;
  }

  private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequestDTO request) {
    // Retrieve the user from the database
    User user = userRepository.findByUsername(request.getUsername());

    if (user != null && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
      );

      // Generate JWT tokens

      CustomUserDetails userDetails = new CustomUserDetails(user);
      String accessToken = jwtUtil.generateToken(userDetails);
      String refreshToken = jwtUtil.generateRefreshToken(userDetails);

     // String accessToken = jwtUtil.generateToken(authentication);
     // String refreshToken = jwtUtil.generateRefreshToken(authentication);

        Map<String, String> body = Map.of("access_token", accessToken);

        // Return access token in body and refresh token in header
        return ResponseEntity.ok()
                .header("Refresh-Token", refreshToken)
                .body(body);
    } else {
        throw new BadCredentialsException("Invalid username or password");
    }
  }

  // Client Token Generation (OAuth Client)
  @PostMapping("/client/api/key")
  public Map<String, String> generateClientToken(@RequestBody OAuthRequestDTO oauthRequest) {
    Client client = clientService.findClientByClientId(oauthRequest.getClientId());
    if (client != null && oauthRequest.getClientSecret().equals(client.getClientSecret())) {
      Map<String, Object> claims = Map.of("role", "CLIENT");

      // Determine expiration
      Long expirationMillis = null;
      if (oauthRequest.getExpiration() != null && oauthRequest.getExpiration() > 0) {
        expirationMillis = oauthRequest.getExpiration() * 1000; // convert to ms
      }

      String accessToken = jwtUtil.generateApiKeyWithClaims(
              oauthRequest.getClientId(), claims, expirationMillis
      );

      Map<String, String> response = new HashMap<>();
      response.put("apiKey", accessToken);
      response.put("type", "bearer");
      if (expirationMillis != null) {
        response.put("expires_in", String.valueOf(expirationMillis / 1000)); // seconds
      } else {
        response.put("expires_in", "never");
      }

      return response;
    } else {
      return Map.of("error", "Invalid client credentials");
    }
  }


  // Refresh Token endpoint
  @PostMapping("/refresh")
  public ResponseEntity<Map<String, String>> refreshToken(@RequestBody RefreshTokenRequestDTO refreshRequest) {
    String username = jwtUtil.getUsername(refreshRequest.getRefreshToken());


    if (username != null && jwtUtil.validateToken(refreshRequest.getRefreshToken())) {
      String newAccessToken = jwtUtil.generateTokenFromUsername(username);

      // Generate a new refresh token (reusing logic from your existing method)
      String newRefreshToken = jwtUtil.generateRefreshTokenFromUsername(username);

      return ResponseEntity.ok()
              .header("Refresh-Token", newRefreshToken)
              .body(Map.of("access_token", newAccessToken));
    } else {
      return ResponseEntity.badRequest()
              .body(Map.of("error", "Invalid refresh token"));
    }
  }
}
