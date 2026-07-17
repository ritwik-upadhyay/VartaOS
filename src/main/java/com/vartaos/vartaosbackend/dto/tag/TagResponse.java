package com.vartaos.vartaosbackend.dto.tag;

import lombok.*;

/**
 * Response DTO returned by Tag APIs.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagResponse {

    /**
     * Tag identifier.
     */
    private Long id;

    /**
     * Tag name.
     */
    private String name;

}