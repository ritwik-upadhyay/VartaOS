package com.vartaos.vartaosbackend.ai.prompt;

import com.vartaos.vartaosbackend.ai.context.AIContext;
import com.vartaos.vartaosbackend.ai.note.NoteGenerationContext;
import com.vartaos.vartaosbackend.ai.note.NotePromptBuilder;
import org.springframework.stereotype.Component;

@Component
public class LearningNotePromptBuilder implements NotePromptBuilder {

    @Override
    public String build(NoteGenerationContext context) {

        return """
            You are the AI tutor for VartaOS.

            Your task is to generate high-quality Learning Notes for a computer science student.

            The notes should:
            - Explain the topic from scratch.
            - Be easy to understand.
            - Use headings and bullet points.
            - Include definitions where appropriate.
            - Include examples if helpful.
            - Explain why the concept is important.
            - Do NOT include revision notes.
            - Do NOT include interview questions.
            - Do NOT include mistakes and learnings.
            - Return only the learning note in Markdown format.

            Topic:
            %s
            """
                .formatted(context.topic());
    }

}