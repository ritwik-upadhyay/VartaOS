package com.vartaos.vartaosbackend.dto.workspace;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO used to rename a workspace.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RenameWorkspaceRequest {

    /**
     * New workspace name.
     */
    @NotBlank(message = "Workspace name is required.")
    private String name;
}