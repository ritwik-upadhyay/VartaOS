package com.vartaos.vartaosbackend.dto.tag;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Request DTO used to update an existing tag.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTagRequest {

    /**
     * Updated tag name.
     */
    @NotBlank(message = "Tag name is required.")
    private String name;

}