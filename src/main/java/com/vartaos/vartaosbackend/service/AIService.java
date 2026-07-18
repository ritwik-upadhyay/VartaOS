package com.vartaos.vartaosbackend.service;

import com.vartaos.vartaosbackend.dto.ai.ChatRequest;
import com.vartaos.vartaosbackend.dto.ai.ChatResponse;
import com.vartaos.vartaosbackend.dto.ai.ConversationResponse;
import com.vartaos.vartaosbackend.dto.ai.CreateConversationRequest;
import com.vartaos.vartaosbackend.dto.ai.MessageResponse;
import java.util.List;

/**
 * Service responsible for interacting with the AI.
 *
 * @author Ritwik
 * @since VartaOS v1
 */
public interface AIService {

    /**
     * Sends a student's message to the AI and returns the reply.
     *
     * @param request Chat request.
     * @return AI response.
     */
    ChatResponse chat(ChatRequest request);

    ConversationResponse createConversation(CreateConversationRequest request);

    List<MessageResponse> getConversationMessages(Long conversationId);

    List<ConversationResponse> getConversations();

}