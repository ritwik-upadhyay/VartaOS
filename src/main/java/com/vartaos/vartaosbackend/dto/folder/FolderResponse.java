package com.vartaos.vartaosbackend.dto.folder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Response DTO returned for folder operations.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolderResponse {

    /**
     * Folder ID.
     */
    private Long id;

    /**
     * Folder name.
     */
    private String name;

    /**
     * Parent folder ID.
     * Null indicates a root folder.
     */
    private Long parentFolderId;

    /**
     * Whether this folder has been completed.
     */
    private Boolean completed;

    /**
     * Child folders of this folder.
     */
    private List<FolderResponse> children;
}
