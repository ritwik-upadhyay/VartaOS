package com.vartaos.vartaosbackend.ai.prompt;

import com.vartaos.vartaosbackend.ai.note.NoteGenerationContext;
import com.vartaos.vartaosbackend.ai.note.NotePromptBuilder;
import org.springframework.stereotype.Component;

@Component
public class InterviewQuestionPromptBuilder implements NotePromptBuilder {

    @Override
    public String build(NoteGenerationContext context) {

        return """
                You are the AI tutor for VartaOS.

                Generate interview questions for the following topic.

                Requirements:
                - Include beginner, intermediate, and advanced questions.
                - Provide concise model answers.
                - Include practical and scenario-based questions where possible.
                - Return only the interview questions in Markdown format.

                Topic:
                %s
                """.formatted(context.topic());
    }
}