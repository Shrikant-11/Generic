package org.authorization.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    UserDetails findByUsername(String userName);
}
