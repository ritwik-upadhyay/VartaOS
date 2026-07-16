package com.vartaos.vartaosbackend.dto.note;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response DTO returned for note operations.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoteResponse {

    /**
     * Note ID.
     */
    private Long id;

    /**
     * Note title.
     */
    private String title;

    /**
     * Note content.
     */
    private String content;

    /**
     * Folder containing this note.
     */
    private Long folderId;
}