package com.vartaos.vartaosbackend.controller;

import com.vartaos.vartaosbackend.dto.ai.ChatRequest;
import com.vartaos.vartaosbackend.dto.ai.ChatResponse;
import com.vartaos.vartaosbackend.service.AIService;
import com.vartaos.vartaosbackend.dto.ai.CreateConversationRequest;
import com.vartaos.vartaosbackend.dto.ai.ConversationResponse;
import lombok.RequiredArgsConstructor;
import com.vartaos.vartaosbackend.dto.ai.MessageResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for AI operations.
 *
 * @author Ritwik
 * @since VartaOS v1
 */
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {

    /**
     * Service responsible for AI interactions.
     */
    private final AIService aiService;

    /**
     * Sends a message to Gemini and returns the response.
     *
     * @param request Student's message.
     * @return AI response.
     */
    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {
        return aiService.chat(request);
    }

    /**
     * Creates a new AI conversation.
     *
     * @param request Conversation creation request.
     * @return Newly created conversation.
     */
    @PostMapping("/conversations")
    public ConversationResponse createConversation(
            @RequestBody CreateConversationRequest request) {

        return aiService.createConversation(request);
    }

    /**
     * Returns all messages of a conversation.
     *
     * @param conversationId Conversation ID.
     * @return Messages ordered by creation time.
     */
    @GetMapping("/conversations/{conversationId}/messages")
    public List<MessageResponse> getConversationMessages(
            @PathVariable Long conversationId) {

        return aiService.getConversationMessages(conversationId);
    }

    /**
     * Returns all AI conversations of the current user.
     *
     * @return List of conversations ordered by latest activity.
     */
    @GetMapping("/conversations")
    public List<ConversationResponse> getConversations() {

        return aiService.getConversations();
    }
}