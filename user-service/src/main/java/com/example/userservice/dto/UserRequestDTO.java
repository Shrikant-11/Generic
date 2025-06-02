package com.example.userservice.dto;

import com.example.userservice.enums.userRoles;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequestDTO {
    @NotNull(message = "Username is required")
    private String username;

    @NotNull(message = "Password is required")
    private String password;

    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    private userRoles role;

    @NotNull(message = "Email is required")
    private String email;

    @NotNull(message = "Mobile Number is required")
    private String mobileNumber;

    private String address;
    private String education;
}
