package com.vartaos.vartaosbackend.dto.folder;

import lombok.*;

/**
 * Request DTO used to move a folder
 * to another parent folder.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoveFolderRequest {

    /**
     * ID of the new parent folder.
     *
     * Null means the folder becomes
     * a root folder.
     */
    private Long parentFolderId;
}