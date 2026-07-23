package com.vartaos.vartaosbackend.ai.prompt;

import com.vartaos.vartaosbackend.ai.context.AIContext;
import org.springframework.stereotype.Component;

@Component
public class ConversationTitlePromptBuilder {

    public String build(AIContext context, String assistantResponse) {

        return """
                Create a short title for this study conversation.

                Requirements:
                - maximum 5 words
                - plain text only
                - no quotes
                - no markdown
                - no punctuation unless essential
                - summarize the topic clearly

                User message:
                %s

                Assistant response:
                %s
                """.formatted(
                context.getUserPrompt(),
                assistantResponse
        );
    }
}
