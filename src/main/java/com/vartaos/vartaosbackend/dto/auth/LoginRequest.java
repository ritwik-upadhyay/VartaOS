package com.vartaos.vartaosbackend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for user authentication login requests.
 * Captures login credentials and performs basic format validation.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    /**
     * The registered email address of the user.
     * Validates that the input is not blank and is structured as a valid email pattern.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    /**
     * Raw password submitted during login.
     * It is compared against the stored BCrypt hash
     * during the authentication process.
     */
    @NotBlank(message = "Password is required")
    private String password;
}
