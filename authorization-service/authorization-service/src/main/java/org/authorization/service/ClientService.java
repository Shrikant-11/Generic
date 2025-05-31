package org.authorization.service;


import org.authorization.domain.Client;

public interface ClientService {
    Client findClientByClientId(String clientId);
}
