package com.vartaos.vartaosbackend.dto.note;

import lombok.*;

/**
 * Request DTO used to update
 * the display order of a note.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoteDisplayOrderRequest {

    /**
     * Note ID.
     */
    private Long id;

    /**
     * New display order.
     */
    private Integer displayOrder;
}