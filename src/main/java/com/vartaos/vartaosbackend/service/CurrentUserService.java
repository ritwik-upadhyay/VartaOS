package com.vartaos.vartaosbackend.service;

import com.vartaos.vartaosbackend.entity.User;
import com.vartaos.vartaosbackend.entity.Workspace;
import com.vartaos.vartaosbackend.repository.UserRepository;
import com.vartaos.vartaosbackend.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;

    public User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));
    }

    public Workspace getCurrentWorkspace() {

        User user = getCurrentUser();

        return workspaceRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Workspace not found."));
    }
}
