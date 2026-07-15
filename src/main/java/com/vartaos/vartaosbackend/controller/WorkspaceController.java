package com.vartaos.vartaosbackend.controller;

import com.vartaos.vartaosbackend.dto.workspace.WorkspaceResponse;
import com.vartaos.vartaosbackend.service.WorkspaceService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.vartaos.vartaosbackend.dto.workspace.RenameWorkspaceRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * REST controller responsible for workspace-related API endpoints.
 */
@RestController
@RequestMapping("/api/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    /**
     * Retrieves the workspace of the currently authenticated user.
     *
     * @param authentication Authentication object provided by Spring Security.
     * @return Workspace information of the logged-in user.
     */
    @GetMapping
    public WorkspaceResponse getWorkspace(Authentication authentication) {

        // Extract the authenticated user's email from the JWT token
        String email = authentication.getName();

        // Retrieve and return the user's workspace
        return workspaceService.getWorkspace(email);
    }

    /**
     * Renames the authenticated user's workspace.
     *
     * @param authentication Authentication object provided by Spring Security.
     * @param request Request containing the new workspace name.
     * @return Updated workspace information.
     */
    @PutMapping
    public WorkspaceResponse renameWorkspace(
            Authentication authentication,
            @Valid @RequestBody RenameWorkspaceRequest request) {

        // Extract authenticated user's email
        String email = authentication.getName();

        // Rename and return updated workspace
        return workspaceService.renameWorkspace(email, request);
    }
}