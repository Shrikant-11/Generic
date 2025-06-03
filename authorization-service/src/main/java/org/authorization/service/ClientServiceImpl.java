package org.authorization.service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

import org.authorization.domain.ApiKey;
import org.authorization.domain.Client;
import org.authorization.dto.ClientRequest;
import org.authorization.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Client findClientByClientId(String clientId) {
        return clientRepository.findByClientId(clientId);
    }

    @Override
    @Transactional
    public String registerClient(ClientRequest clientRequest) {
        if (clientRepository.findByClientId(clientRequest.getClientId()) != null) {
            throw new RuntimeException("Client already exists");
        }

        // Create API key first
        String ramdomPrefix = UUID.randomUUID().toString().substring(0, 10);
        String secretKey = UUID.randomUUID().toString();

        ApiKey apiKey = ApiKey.builder()
                .keyValue(ramdomPrefix + "." + BCrypt.hashpw(secretKey, BCrypt.gensalt()))
                .name(clientRequest.getClientName())
                .active(true)
                .expiresAt(LocalDateTime.now().plusDays(30))
                .createdAt(LocalDateTime.now())
                .build();

        // Create client with the API key
        Client client = Client.builder()
                .clientId(clientRequest.getClientId())
                .clientSecret(Base64.getEncoder().encodeToString(clientRequest.getClientSecret().getBytes()))
                .clientName(clientRequest.getClientName())
                .active(true)
                .createdAt(LocalDateTime.now())
                .lastAccessedAt(LocalDateTime.now())
                .apiKey(apiKey)
                .build();

        clientRepository.save(client);

        // return the secret key
        return ramdomPrefix + "." + secretKey;
    }
}
