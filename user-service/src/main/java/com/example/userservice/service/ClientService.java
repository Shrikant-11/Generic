package com.example.userservice.service;


import com.example.userservice.domain.Client;

public interface ClientService {
    Client findClientByClientId(String clientId);
}
