package com.vartaos.vartaosbackend.ai.prompt;

import com.vartaos.vartaosbackend.ai.context.AIContext;
import com.vartaos.vartaosbackend.ai.note.NoteGenerationContext;
import com.vartaos.vartaosbackend.ai.note.NotePromptBuilder;
import org.springframework.stereotype.Component;

@Component
public class MistakesLearningPromptBuilder implements NotePromptBuilder {

    @Override
    public String build(NoteGenerationContext context) {

        return """
                You are the AI tutor for VartaOS.

                Generate a "Mistakes & Learnings" note for the following topic.

                Include:
                - Common mistakes students make.
                - Interview traps.
                - Edge cases.
                - Best practices.
                - Important things to remember.

                Return only the note in Markdown format.

                Topic:
                %s
                """.formatted(context.topic());
    }
}