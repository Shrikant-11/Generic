package com.example.userservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequestDTO {
    @NotNull(message = "Username is required")
    private String username;

    @NotNull(message = "Password is required")
    private String password;

    @NotNull(message = "Role is required")
    private String role;

    @NotNull(message = "Email is required")
    private String email;

    @NotNull(message = "Mobile Number is required")
    private String mobileNumber;

    private String address;
    private String education;
}
