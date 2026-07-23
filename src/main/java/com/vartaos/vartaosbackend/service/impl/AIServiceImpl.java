package com.vartaos.vartaosbackend.service.impl;

import com.vartaos.vartaosbackend.ai.context.AIContext;
import com.vartaos.vartaosbackend.ai.context.ContextBuilder;
import com.vartaos.vartaosbackend.ai.prompt.ConversationTitlePromptBuilder;
import com.vartaos.vartaosbackend.ai.prompt.TutorPromptBuilder;
import com.vartaos.vartaosbackend.dto.ai.ChatRequest;
import com.vartaos.vartaosbackend.dto.ai.ChatResponse;
import com.vartaos.vartaosbackend.service.AIService;
import com.vartaos.vartaosbackend.service.CurrentUserService;
import com.vartaos.vartaosbackend.service.provider.AIProvider;
import com.vartaos.vartaosbackend.service.provider.AIProviderRegistry;
import com.vartaos.vartaosbackend.service.provider.AITask;
import lombok.RequiredArgsConstructor;
import com.vartaos.vartaosbackend.entity.AIConversation;
import com.vartaos.vartaosbackend.entity.AIMessage;
import com.vartaos.vartaosbackend.entity.enums.MessageSender;
import com.vartaos.vartaosbackend.repository.AIConversationRepository;
import com.vartaos.vartaosbackend.repository.AIMessageRepository;
import com.vartaos.vartaosbackend.dto.ai.ConversationResponse;
import com.vartaos.vartaosbackend.dto.ai.CreateConversationRequest;
import com.vartaos.vartaosbackend.entity.Workspace;
import com.vartaos.vartaosbackend.entity.User;
import com.vartaos.vartaosbackend.dto.ai.MessageResponse;


import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Default implementation of AIService.
 *
 * @author Ritwik
 * @since VartaOS v1
 */
@Service
@RequiredArgsConstructor
public class AIServiceImpl implements AIService {

    private final AIProviderRegistry aiProviderRegistry;
    private final TutorPromptBuilder promptBuilder;
    private final ConversationTitlePromptBuilder conversationTitlePromptBuilder;
    private final ContextBuilder contextBuilder;

    private final AIConversationRepository conversationRepository;
    private final AIMessageRepository messageRepository;

    private final CurrentUserService currentUserService;

    /**
     * Sends a message to the AI.
     * <p>
     * Actual implementation will be added in the next step.
     */
    @Override
    public ChatResponse chat(ChatRequest request) {


        AIContext context = contextBuilder.build(request);

        AIConversation conversation = context.getConversation();

        AIMessage userMessage = AIMessage.builder()
                .conversation(conversation)
                .sender(MessageSender.USER)
                .content(request.getMessage())
                .build();

        messageRepository.save(userMessage);


        String prompt = promptBuilder.build(context);

        User user = currentUserService.getCurrentUser();
        AIProvider aiProvider = aiProviderRegistry.resolveProvider(user);
        String model = aiProviderRegistry.resolveModel(user, aiProvider);

        String aiResponse = aiProvider.generate(AITask.CHAT, prompt, model);

        if (shouldGenerateTitle(conversation)) {
            String titlePrompt = conversationTitlePromptBuilder.build(context, aiResponse);
            String generatedTitle = aiProvider.generate(AITask.CHAT_TITLE, titlePrompt, model);
            conversation.setTitle(normalizeConversationTitle(generatedTitle));
            conversationRepository.save(conversation);
        }

        AIMessage aiMessage = AIMessage.builder()
                .conversation(conversation)
                .sender(MessageSender.AI)
                .content(aiResponse)
                .build();

        messageRepository.save(aiMessage);

        return ChatResponse.builder()
                .response(aiResponse)
                .build();

    }

    @Override
    public ConversationResponse createConversation(CreateConversationRequest request) {

        Workspace workspace = currentUserService.getCurrentWorkspace();

        AIConversation conversation = AIConversation.builder()
                .title(
                        request.getTitle() == null || request.getTitle().isBlank()
                                ? "New Chat"
                                : request.getTitle()
                )
                .workspace(workspace)
                .build();

        conversation = conversationRepository.save(conversation);

        return ConversationResponse.builder()
                .id(conversation.getId())
                .title(conversation.getTitle())
                .build();
    }

    @Override
    public List<MessageResponse> getConversationMessages(Long conversationId) {


        AIConversation conversation = conversationRepository
                .findById(conversationId)
                .orElseThrow(() ->
                        new RuntimeException("Conversation not found.")
                );

        return messageRepository
                .findByConversationOrderByCreatedAtAsc(conversation)
                .stream()
                .map(message -> MessageResponse.builder()
                        .sender(message.getSender())
                        .content(message.getContent())
                        .createdAt(message.getCreatedAt())
                        .build())
                .toList();
    }

    @Override
    public List<ConversationResponse> getConversations() {

        Workspace workspace = currentUserService.getCurrentWorkspace();

        return conversationRepository
                .findByWorkspaceOrderByUpdatedAtDesc(workspace)
                .stream()
                .map(conversation -> ConversationResponse.builder()
                        .id(conversation.getId())
                        .title(conversation.getTitle())
                        .build())
                .toList();
    }

    private boolean shouldGenerateTitle(AIConversation conversation) {
        String title = conversation.getTitle();
        return title == null
                || title.isBlank()
                || "New Chat".equalsIgnoreCase(title)
                || "AI Discussion".equalsIgnoreCase(title);
    }

    private String normalizeConversationTitle(String generatedTitle) {

        if (generatedTitle == null || generatedTitle.isBlank()) {
            return "AI Discussion";
        }

        String normalized = generatedTitle
                .replace("\"", "")
                .replace("`", "")
                .trim();

        String singleLine = normalized.lines()
                .findFirst()
                .orElse("AI Discussion")
                .trim();

        return singleLine.isBlank()
                ? "AI Discussion"
                : singleLine;
    }
}
