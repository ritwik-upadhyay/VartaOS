package com.vartaos.vartaosbackend.dto.folder;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO used to rename an existing folder.
 */
@Getter
@Setter
public class RenameFolderRequest {

    /**
     * New folder name.
     */
    @NotBlank(message = "Folder name is required.")
    private String name;
}