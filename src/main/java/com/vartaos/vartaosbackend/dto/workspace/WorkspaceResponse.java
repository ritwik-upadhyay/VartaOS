package com.vartaos.vartaosbackend.dto.workspace;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO returned when workspace information is requested.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceResponse {

    /**
     * Workspace ID.
     */
    private Long id;

    /**
     * Workspace name.
     */
    private String name;
}