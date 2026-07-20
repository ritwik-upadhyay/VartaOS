package com.vartaos.vartaosbackend.ai.context;

import com.vartaos.vartaosbackend.entity.AIConversation;
import com.vartaos.vartaosbackend.entity.AIMessage;
import com.vartaos.vartaosbackend.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AIContext {

    private final AIConversation conversation;

    private final List<AIMessage> conversationHistory;

    private final String userPrompt;

    private final User user;

}