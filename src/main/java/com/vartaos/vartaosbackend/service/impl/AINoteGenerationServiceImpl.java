package com.vartaos.vartaosbackend.service.impl;

import com.vartaos.vartaosbackend.ai.note.NoteGenerationContext;
import com.vartaos.vartaosbackend.ai.prompt.InterviewQuestionPromptBuilder;
import com.vartaos.vartaosbackend.ai.prompt.LearningNotePromptBuilder;
import com.vartaos.vartaosbackend.ai.prompt.MistakesLearningPromptBuilder;
import com.vartaos.vartaosbackend.ai.prompt.RevisionNotePromptBuilder;
import com.vartaos.vartaosbackend.entity.Folder;
import com.vartaos.vartaosbackend.entity.Note;
import com.vartaos.vartaosbackend.entity.enums.NoteType;
import com.vartaos.vartaosbackend.repository.FolderRepository;
import com.vartaos.vartaosbackend.repository.NoteRepository;
import com.vartaos.vartaosbackend.service.AINoteGenerationService;
import com.vartaos.vartaosbackend.service.provider.GeminiProvider;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AINoteGenerationServiceImpl
        implements AINoteGenerationService {

    private final FolderRepository folderRepository;

    private final NoteRepository noteRepository;

    private final GeminiProvider geminiProvider;

    private final LearningNotePromptBuilder learningPromptBuilder;

    private final RevisionNotePromptBuilder revisionPromptBuilder;

    private final InterviewQuestionPromptBuilder interviewPromptBuilder;

    private final MistakesLearningPromptBuilder mistakesPromptBuilder;

    private Note createNote(
            Folder folder,
            String title,
            String content,
            NoteType type
    ) {

        return Note.builder()
                .title(title)
                .content(content)
                .type(type)
                .folder(folder)
                .displayOrder(0)
                .build();
    }



    @Override
    public void generateNotes(Long folderId) {

        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        NoteGenerationContext context =
                new NoteGenerationContext(folder.getName());

        String learningPrompt = learningPromptBuilder.build(context);

        String revisionPrompt = revisionPromptBuilder.build(context);

        String interviewPrompt = interviewPromptBuilder.build(context);

        String mistakesPrompt = mistakesPromptBuilder.build(context);

        String learningContent = geminiProvider.generateText(learningPrompt);

        String revisionContent = geminiProvider.generateText(revisionPrompt);

        String interviewContent = geminiProvider.generateText(interviewPrompt);

        String mistakesContent = geminiProvider.generateText(mistakesPrompt);

        Note learningNote = createNote(
                folder,
                "Learning Note",
                learningContent,
                NoteType.LEARNING
        );

        Note revisionNote = createNote(
                folder,
                "Revision Note",
                revisionContent,
                NoteType.REVISION
        );

        Note interviewNote = createNote(
                folder,
                "Interview Questions",
                interviewContent,
                NoteType.INTERVIEW
        );

        Note mistakesNote = createNote(
                folder,
                "Mistakes & Learnings",
                mistakesContent,
                NoteType.MISTAKES
        );

        noteRepository.saveAll(
                List.of(
                        learningNote,
                        revisionNote,
                        interviewNote,
                        mistakesNote
                )
        );
    }

    @Override
    public void markFolderCompleted(Long folderId) {

        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        folder.setCompleted(true);

        folderRepository.save(folder);
    }

}