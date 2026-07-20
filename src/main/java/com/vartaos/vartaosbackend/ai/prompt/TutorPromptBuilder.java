package com.vartaos.vartaosbackend.ai.prompt;

import com.vartaos.vartaosbackend.ai.context.AIContext;
import org.springframework.stereotype.Component;

@Component
public class TutorPromptBuilder implements PromptBuilder {

    @Override
    public String build(AIContext context) {

        return """
                You are the AI tutor for VartaOS.

                Respond ONLY with valid JSON.

                Use exactly this structure:

                {
                  "title": "Short conversation title (maximum 5 words)",
                  "response": "Your helpful answer"
                }

                Rules:
                - Return only JSON.
                - Do not use markdown.
                - Do not wrap the JSON inside ``` blocks.
                - Keep the title concise.
                - The response field should contain the complete answer.

                User message:
                %s
                """
                .formatted(context.getUserPrompt());

    }

}