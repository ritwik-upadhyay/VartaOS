package com.vartaos.vartaosbackend.dto.note;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request DTO used to update an existing note.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNoteRequest {

    /**
     * Updated note title.
     */
    @NotBlank(message = "Title is required.")
    private String title;

    /**
     * Updated note content.
     */
    private String content;
}