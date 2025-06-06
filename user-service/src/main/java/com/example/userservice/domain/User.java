package com.example.userservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    // account_expired , account_locked , email , mobile_number, addreess, education
    @Column(nullable = false)
    private boolean accountExpired = false;

    @Column(nullable = false)
    private boolean accountLocked = false;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String mobileNumber;

    @Column(nullable = true)
    private String address;

    @Column(nullable = true)
    private String education;

}
