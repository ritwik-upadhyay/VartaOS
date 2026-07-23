package com.vartaos.vartaosbackend.controller;

import com.vartaos.vartaosbackend.dto.ai.ChatRequest;
import com.vartaos.vartaosbackend.dto.ai.ChatResponse;
import com.vartaos.vartaosbackend.dto.ai.AIProviderOptionResponse;
import com.vartaos.vartaosbackend.dto.ai.AISettingsResponse;
import com.vartaos.vartaosbackend.dto.response.ApiResponse;
import com.vartaos.vartaosbackend.service.AINoteGenerationService;
import com.vartaos.vartaosbackend.service.AIService;
import com.vartaos.vartaosbackend.service.AISettingsService;
import com.vartaos.vartaosbackend.dto.ai.CreateConversationRequest;
import com.vartaos.vartaosbackend.dto.ai.ConversationResponse;
import com.vartaos.vartaosbackend.dto.ai.UpdateAISettingsRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.vartaos.vartaosbackend.dto.ai.MessageResponse;
import org.springframework.http.ResponseEntity;
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

    private final AINoteGenerationService aiNoteGenerationService;

    private final AISettingsService aiSettingsService;

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

    @GetMapping("/settings")
    public AISettingsResponse getAiSettings() {
        return aiSettingsService.getSettings();
    }

    @PutMapping("/settings")
    public AISettingsResponse updateAiSettings(
            @Valid @RequestBody UpdateAISettingsRequest request) {
        return aiSettingsService.updateSettings(request);
    }

    @GetMapping("/providers")
    public List<AIProviderOptionResponse> getAiProviders() {
        return aiSettingsService.getProviderOptions();
    }

    @PostMapping("/folders/{folderId}/generate-notes")
    public ResponseEntity<ApiResponse> generateNotes(
            @PathVariable Long folderId
    ) {

        aiNoteGenerationService.generateNotes(folderId);

        return ResponseEntity.ok(
                new ApiResponse("Notes generated successfully.")
        );
    }

    @PatchMapping("/folders/{folderId}/complete")
    public ResponseEntity<ApiResponse> markFolderCompleted(
            @PathVariable Long folderId
    ) {

        aiNoteGenerationService.markFolderCompleted(folderId);

        return ResponseEntity.ok(
                new ApiResponse("Topic marked as completed.")
        );
    }
}
