package com.vartaos.vartaosbackend.dto.note;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request DTO used to create a new note.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNoteRequest {

    /**
     * Title of the note.
     */
    @NotBlank(message = "Title is required.")
    private String title;

    /**
     * Content of the note.
     */
    private String content;

    /**
     * Folder in which the note should be created.
     */
    private Long folderId;
}