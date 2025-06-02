package com.example.userservice.repository;

import com.example.userservice.domain.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {

    @Query("SELECT a FROM ApiKey a WHERE a.active = true AND a.keyValue LIKE CONCAT(:prefix, '.%')")
    Optional<ApiKey> findByPrefixAndActiveTrue(String prefix);
}