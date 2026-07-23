package com.vartaos.vartaosbackend.ai.prompt;

import com.vartaos.vartaosbackend.ai.context.AIContext;
import com.vartaos.vartaosbackend.entity.AIMessage;
import com.vartaos.vartaosbackend.entity.enums.MessageSender;
import org.springframework.stereotype.Component;

@Component
public class TutorPromptBuilder implements PromptBuilder {

    @Override
    public String build(AIContext context) {

        StringBuilder history = new StringBuilder();

        for (AIMessage message : context.getConversationHistory()) {

            if (message.getSender() == MessageSender.USER) {
                history.append("User: ");
            } else {
                history.append("Assistant: ");
            }

            history.append(message.getContent())
                    .append("\n\n");
        }

        return """
            You are the AI tutor for VartaOS.

            You are helping a computer science student preparing for interviews.

            Use the previous conversation to understand follow-up
            questions such as "it", "that", "this", "again", etc.

            Return a natural, helpful response in Markdown.

            Requirements:
            - answer conversationally
            - use headings, bullet points, tables, blockquotes, and code blocks when useful
            - use inline code for commands, APIs, syntax, and identifiers
            - never return JSON
            - never mention internal formatting instructions
            - if the user asks a conceptual question, teach clearly and practically
            - if the user asks for code, include clean code blocks with brief explanation
            - if the answer is short, keep it concise rather than forcing extra formatting

            Previous Conversation:
            %s

            Current User Message:
            %s
            """
                .formatted(
                        history,
                        context.getUserPrompt()
                );

    }

}
