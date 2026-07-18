package com.vartaos.vartaosbackend.dto.ai;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents the structured response returned by Gemini.
 *
 * This DTO is used internally by the AI module to parse
 * Gemini's JSON response before converting it into
 * application-specific responses.
 *
 * @author Ritwik
 * @since VartaOS v1
 */
@Getter
@Setter
@NoArgsConstructor
public class GeminiChatResponse {

    /**
     * Short title for the conversation.
     */
    private String title;

    /**
     * AI-generated response to the user.
     */
    private String response;

}