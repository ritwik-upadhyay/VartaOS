package com.vartaos.vartaosbackend.ai.context;

import com.vartaos.vartaosbackend.dto.ai.ChatRequest;
import com.vartaos.vartaosbackend.entity.AIConversation;
import com.vartaos.vartaosbackend.entity.AIMessage;
import com.vartaos.vartaosbackend.repository.AIConversationRepository;
import com.vartaos.vartaosbackend.repository.AIMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
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

        Pageable pageable = PageRequest.of(0, 20);

        List<AIMessage> history = new ArrayList<>(
                messageRepository
                        .findByConversationOrderByCreatedAtDesc(
                                conversation,
                                pageable
                        )
                        .getContent()
        );

        Collections.reverse(history);

        return AIContext.builder()
                .conversation(conversation)
                .conversationHistory(history)
                .userPrompt(request.getMessage())
                .build();

    }

}
