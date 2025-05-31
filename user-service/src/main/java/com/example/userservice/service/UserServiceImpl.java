package com.example.userservice.service;

import com.example.userservice.domain.User;
import com.example.userservice.dto.UserRequestDTO;
import com.example.userservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(UserRequestDTO userRequestDTO) {
        User user = User.builder()
            .username(userRequestDTO.getUsername())
            .password(passwordEncoder.encode(userRequestDTO.getPassword()))
            .role(userRequestDTO.getRole())
            .email(userRequestDTO.getEmail())
            .mobileNumber(userRequestDTO.getMobileNumber())
            .address(userRequestDTO.getAddress())
            .education(userRequestDTO.getEducation())
            .build();
        return userRepository.save(user);
    }
}
