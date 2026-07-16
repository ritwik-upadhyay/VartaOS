package com.vartaos.vartaosbackend.dto.note;

import lombok.*;

/**
 * Request DTO used to move a note
 * to another folder.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoveNoteRequest {

    /**
     * ID of the destination folder.
     */
    private Long folderId;
}