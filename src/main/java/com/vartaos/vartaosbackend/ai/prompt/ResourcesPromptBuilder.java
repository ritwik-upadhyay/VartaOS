package com.vartaos.vartaosbackend.ai.prompt;

import com.vartaos.vartaosbackend.ai.note.NoteGenerationContext;
import com.vartaos.vartaosbackend.ai.note.NotePromptBuilder;
import org.springframework.stereotype.Component;

@Component
public class ResourcesPromptBuilder implements NotePromptBuilder {

    @Override
    public String build(NoteGenerationContext context) {

        return """
                Create a Markdown resources note for the computer science topic: %s

                The note must include:
                - Core concepts to revise
                - Recommended practice patterns
                - Common pitfalls
                - Interview follow-up angles
                - A short checklist for next study steps

                Keep it structured, concise, and practical.
                Return only the resources note in Markdown format.
                """.formatted(context.topic());
    }
}
