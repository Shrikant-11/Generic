package com.example.userservice.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.example.userservice.domain.User;
import com.example.userservice.dto.UserRequestDTO;

public interface UserService {
    
    User registerUser(UserRequestDTO userRequestDTO);
}
