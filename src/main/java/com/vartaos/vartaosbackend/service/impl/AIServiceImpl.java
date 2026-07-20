package com.vartaos.vartaosbackend.service.impl;

import com.vartaos.vartaosbackend.ai.context.AIContext;
import com.vartaos.vartaosbackend.ai.context.ContextBuilder;
import com.vartaos.vartaosbackend.ai.prompt.PromptBuilder;
import com.vartaos.vartaosbackend.dto.ai.ChatRequest;
import com.vartaos.vartaosbackend.dto.ai.ChatResponse;
import com.vartaos.vartaosbackend.service.AIService;
import com.vartaos.vartaosbackend.service.provider.AIProvider;
import lombok.RequiredArgsConstructor;
import com.vartaos.vartaosbackend.entity.AIConversation;
import com.vartaos.vartaosbackend.entity.AIMessage;
import com.vartaos.vartaosbackend.entity.enums.MessageSender;
import com.vartaos.vartaosbackend.repository.AIConversationRepository;
import com.vartaos.vartaosbackend.repository.AIMessageRepository;
import com.vartaos.vartaosbackend.dto.ai.ConversationResponse;
import com.vartaos.vartaosbackend.dto.ai.CreateConversationRequest;
import com.vartaos.vartaosbackend.entity.Workspace;
import com.vartaos.vartaosbackend.repository.WorkspaceRepository;
import com.vartaos.vartaosbackend.repository.UserRepository;
import com.vartaos.vartaosbackend.entity.User;
import org.springframework.security.core.Authentication;
import com.vartaos.vartaosbackend.dto.ai.GeminiChatResponse;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final AIProvider aiProvider;
    private final PromptBuilder promptBuilder;
    private final ContextBuilder contextBuilder;

    private final AIConversationRepository conversationRepository;
    private final AIMessageRepository messageRepository;

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;

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

        GeminiChatResponse geminiResponse = aiProvider.chat(prompt);

        if ("New Chat".equals(conversation.getTitle())) {
            conversation.setTitle(geminiResponse.getTitle());
            conversationRepository.save(conversation);
        }

        System.out.println("Title: " + geminiResponse.getTitle());
        System.out.println("Response: " + geminiResponse.getResponse());

        AIMessage aiMessage = AIMessage.builder()
                .conversation(conversation)
                .sender(MessageSender.AI)
                .content(geminiResponse.getResponse())
                .build();

        messageRepository.save(aiMessage);

        return ChatResponse.builder()
                .response(geminiResponse.getResponse())
                .build();

    }

    @Override
    public ConversationResponse createConversation(CreateConversationRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found.")
                );

        Workspace workspace = workspaceRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Workspace not found.")
                );

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

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found.")
                );

        Workspace workspace = workspaceRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Workspace not found.")
                );

        return conversationRepository
                .findByWorkspaceOrderByUpdatedAtDesc(workspace)
                .stream()
                .map(conversation -> ConversationResponse.builder()
                        .id(conversation.getId())
                        .title(conversation.getTitle())
                        .build())
                .toList();
    }
}