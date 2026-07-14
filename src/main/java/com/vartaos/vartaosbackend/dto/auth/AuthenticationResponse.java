package com.vartaos.vartaosbackend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object returned after authentication operations.
 * Contains information about the authenticated user along with
 * a status message indicating the result of the operation.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {

    /**
     * Feedback or status message returned to the client.
     */
    private String message;

    /**
     * Username of the authenticated user.
     */
    private String username;

    /**
     * Email address of the authenticated user.
     */
    private String email;
}
