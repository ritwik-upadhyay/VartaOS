package com.vartaos.vartaosbackend.ai.context;

import com.vartaos.vartaosbackend.dto.ai.ChatRequest;
import com.vartaos.vartaosbackend.entity.AIConversation;
import com.vartaos.vartaosbackend.entity.AIMessage;
import com.vartaos.vartaosbackend.repository.AIConversationRepository;
import com.vartaos.vartaosbackend.repository.AIMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ContextBuilder {

    private final AIConversationRepository conversationRepository;
    private final AIMessageRepository messageRepository;

    public AIContext build(ChatRequest request) {

        AIConversation conversation =
                conversationRepository.findById(request.getConversationId())
                        .orElseThrow(() ->
                                new RuntimeException("Conversation not found.")
                        );

        List<AIMessage> history =
                messageRepository.findByConversationOrderByCreatedAtAsc(conversation);

        return AIContext.builder()
                .conversation(conversation)
                .conversationHistory(history)
                .userPrompt(request.getMessage())
                .build();

    }

}