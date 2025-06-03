package org.authorization.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientRequest {

    @NotNull(message = "Client ID is required")
    private String clientId;

    @NotNull(message = "Client Secret is required")
    private String clientSecret;

    @NotNull(message = "Client Name is required")
    private String clientName;
}