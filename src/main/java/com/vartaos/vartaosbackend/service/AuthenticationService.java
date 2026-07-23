package com.vartaos.vartaosbackend.service;

import com.vartaos.vartaosbackend.dto.auth.AuthenticationResponse;
import com.vartaos.vartaosbackend.dto.auth.LoginRequest;
import com.vartaos.vartaosbackend.dto.auth.RegisterRequest;
import com.vartaos.vartaosbackend.entity.User;
import com.vartaos.vartaosbackend.entity.Workspace;
import com.vartaos.vartaosbackend.entity.enums.AIProviderType;
import com.vartaos.vartaosbackend.exception.AuthenticationException;
import com.vartaos.vartaosbackend.repository.UserRepository;
import com.vartaos.vartaosbackend.repository.WorkspaceRepository;
import com.vartaos.vartaosbackend.entity.Progress;
import com.vartaos.vartaosbackend.repository.ProgressRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service responsible for authentication and account-related
 * business logic such as registration, login, password
 * management, and account lifecycle operations.
 */
@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final WorkspaceRepository workspaceRepository;
    private final ProgressRepository progressRepository;


    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 WorkspaceRepository workspaceRepository,
                                 ProgressRepository progressRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.workspaceRepository = workspaceRepository;
        this.progressRepository = progressRepository;
    }

    public AuthenticationResponse register(RegisterRequest request) {

        //Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AuthenticationException("Username already exists.");
        }

        //Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthenticationException("Email is already registered.");
        }

        //Create User entity
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .preferredAiProvider(AIProviderType.GEMINI)
                .build();

        //Save to database
        userRepository.save(user);

        // Create a personalized default workspace for the new user
        String displayName = user.getUsername().substring(0, 1).toUpperCase()
                + user.getUsername().substring(1);

        Workspace workspace = Workspace.builder()
                .name(displayName + "'s Workspace")
                .user(user)
                .build();

        workspaceRepository.save(workspace);

        Progress progress = Progress.builder()
                .workspace(workspace)
                .build();

        progressRepository.save(progress);

        //Return response
        return AuthenticationResponse.builder()
                .message("Registration successful.")
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    public AuthenticationResponse login(LoginRequest request) {

        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthenticationException("Invalid email or password."));

        // Compare raw password with BCrypt hash
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Invalid email or password.");
        }

        //Generate JWT
        String token = jwtService.generateToken(user.getEmail());

        // Return success response
        return AuthenticationResponse.builder()
                .message("Login successful.")
                .username(user.getUsername())
                .email(user.getEmail())
                .token(token)
                .build();
    }
}
