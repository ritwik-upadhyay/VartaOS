package com.vartaos.vartaosbackend.dto.tag;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Request DTO used to create a new tag.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTagRequest {

    /**
     * Name of the tag.
     */
    @NotBlank(message = "Tag name is required.")
    private String name;

}