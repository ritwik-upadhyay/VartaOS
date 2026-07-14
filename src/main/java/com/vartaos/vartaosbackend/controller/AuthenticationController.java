package com.vartaos.vartaosbackend.controller;

import com.vartaos.vartaosbackend.dto.auth.AuthenticationResponse;
import com.vartaos.vartaosbackend.dto.auth.RegisterRequest;
import com.vartaos.vartaosbackend.dto.auth.LoginRequest;
import com.vartaos.vartaosbackend.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller responsible for handling authentication-related
 * HTTP requests such as user registration and login.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public AuthenticationResponse register(
            @Valid @RequestBody RegisterRequest request) {
        return authenticationService.register(request);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(
            @Valid @RequestBody LoginRequest request) {
        return authenticationService.login(request);

    }
}
