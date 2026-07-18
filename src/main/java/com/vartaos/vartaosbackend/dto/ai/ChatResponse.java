package com.vartaos.vartaosbackend.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response returned after the AI generates a reply.
 *
 * @author Ritwik
 * @since VartaOS v1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatResponse {

    /**
     * AI-generated reply.
     */
    private String response;
}