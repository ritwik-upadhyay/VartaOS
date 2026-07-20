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

            You are helping a computer science student.

            Use the previous conversation to understand follow-up
            questions such as "it", "that", "this", "again", etc.

            Respond ONLY with valid JSON.
            
            Never include explanations outside the JSON.
            Never use Markdown.
            Never wrap the response in ```json.
            Never include additional text before or after the JSON.
                
            Return exactly:
           
            {
              "title": "Short conversation title (maximum 5 words)",
              "response": "Your helpful answer"
            }

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