package com.example.userservice.service;


import com.example.userservice.domain.Client;
import com.example.userservice.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Client findClientByClientId(String clientId) {
        return clientRepository.findByClientId(clientId);
    }
}
