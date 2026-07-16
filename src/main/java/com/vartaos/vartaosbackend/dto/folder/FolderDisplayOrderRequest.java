package com.vartaos.vartaosbackend.dto.folder;

import lombok.*;

/**
 * Request DTO used to update
 * the display order of a folder.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolderDisplayOrderRequest {

    /**
     * Folder ID.
     */
    private Long id;

    /**
     * New display order.
     */
    private Integer displayOrder;
}