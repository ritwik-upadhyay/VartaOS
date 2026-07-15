package com.vartaos.vartaosbackend.dto.folder;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO used to create a new folder.
 */
@Getter
@Setter
public class CreateFolderRequest {

    /**
     * Name of the new folder.
     */
    @NotBlank(message = "Folder name is required.")
    private String name;

    /**
     * Parent folder ID.
     * Null indicates a root-level folder.
     */
    private Long parentFolderId;
}