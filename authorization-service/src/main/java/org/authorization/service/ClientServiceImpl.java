package org.authorization.service;

import org.authorization.domain.Client;
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
}
