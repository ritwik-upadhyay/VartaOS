package com.vartaos.vartaosbackend.exception;

/**
 * Thrown when authentication or authorization
 * related operations fail.
 */
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }
}