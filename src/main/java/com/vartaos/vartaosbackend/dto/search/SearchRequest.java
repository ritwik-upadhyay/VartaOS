package com.vartaos.vartaosbackend.dto.search;

import lombok.*;

/**
 * Request DTO used for workspace search.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

    /**
     * Keyword entered by the user.
     */
    private String keyword;

}