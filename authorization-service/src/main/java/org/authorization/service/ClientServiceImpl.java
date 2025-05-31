package org.authorization.service;

import java.util.Base64;
import java.util.UUID;

import org.authorization.domain.Client;
import org.authorization.dto.ClientRequest;
import org.authorization.repository.ClientRepository;
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

    @Override
    public String registerClient(ClientRequest clientRequest) {
        if (clientRepository.findByClientId(clientRequest.getClientId()) != null) {
            throw new RuntimeException("Client already exists");
        }
        
        

        Client client = Client.builder()
            .clientId(clientRequest.getClientId())
            .clientSecret(Base64.getEncoder().encodeToString(clientRequest.getClientSecret().getBytes()))
            .clientName(clientRequest.getClientName())
            .build();

         clientRepository.save(client);

        // create secret API key which will be associated with the client
        String secretApiKey = UUID.randomUUID().toString();
        client.setSecretApiKey(secretApiKey);

        return secretApiKey;
    
    }
}
