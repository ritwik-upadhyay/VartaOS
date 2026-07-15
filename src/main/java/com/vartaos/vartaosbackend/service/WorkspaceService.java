package com.vartaos.vartaosbackend.service;

import com.vartaos.vartaosbackend.dto.workspace.WorkspaceResponse;
import com.vartaos.vartaosbackend.entity.User;
import com.vartaos.vartaosbackend.entity.Workspace;
import com.vartaos.vartaosbackend.exception.AuthenticationException;
import com.vartaos.vartaosbackend.repository.UserRepository;
import com.vartaos.vartaosbackend.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;
import com.vartaos.vartaosbackend.dto.workspace.RenameWorkspaceRequest;

/**
 * Service responsible for workspace-related business logic,
 * such as retrieving and managing a user's workspace.
 */
@Service
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;

    public WorkspaceService(WorkspaceRepository workspaceRepository,
                            UserRepository userRepository) {
        this.workspaceRepository = workspaceRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves the workspace belonging to the authenticated user.
     *
     * @param email Email address of the authenticated user.
     * @return Workspace information.
     */

    public WorkspaceResponse getWorkspace(String email) {

        // Find the authenticated user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("User not found."));

        // Retrieve the workspace associated with the user
        Workspace workspace = workspaceRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Workspace not found."));

        // Convert entity into response DTO
        return WorkspaceResponse.builder()
                .id(workspace.getId())
                .name(workspace.getName())
                .build();
    }

    /**
     * Renames the workspace belonging to the authenticated user.
     *
     * @param email   Email of the authenticated user.
     * @param request Request containing the new workspace name.
     * @return Updated workspace information.
     */
    public WorkspaceResponse renameWorkspace(String email,
                                             RenameWorkspaceRequest request) {

        // Find authenticated user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("User not found."));

        // Find user's workspace
        Workspace workspace = workspaceRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Workspace not found."));

        // Update workspace name
        workspace.setName(request.getName());

        // Save updated workspace
        workspaceRepository.save(workspace);

        // Return updated workspace
        return WorkspaceResponse.builder()
                .id(workspace.getId())
                .name(workspace.getName())
                .build();
    }
}