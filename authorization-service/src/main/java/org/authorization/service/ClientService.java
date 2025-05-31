package org.authorization.service;


import org.authorization.domain.Client;
import org.authorization.dto.ClientRequest;

public interface ClientService {
    Client findClientByClientId(String clientId);
    String registerClient(ClientRequest clientRequest);
}
