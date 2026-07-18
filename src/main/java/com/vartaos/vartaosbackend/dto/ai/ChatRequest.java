package com.vartaos.vartaosbackend.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request sent by the client when chatting with the AI.
 *
 * @author Ritwik
 * @since VartaOS v1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRequest {

    /**
     * ID of the conversation.
     */
    private Long conversationId;

    /**
     * Message entered by the student.
     */
    private String message;
}