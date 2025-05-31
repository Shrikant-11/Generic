package com.example.userservice.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthRequestDTO{
    private String clientId;
    private String clientSecret;
    private Long expiration;
}
