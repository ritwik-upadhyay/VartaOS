package com.vartaos.vartaosbackend.service;

import com.vartaos.vartaosbackend.dto.note.CreateNoteRequest;
import com.vartaos.vartaosbackend.dto.note.NoteResponse;
import com.vartaos.vartaosbackend.dto.note.UpdateNoteRequest;
import com.vartaos.vartaosbackend.dto.note.MoveNoteRequest;
import com.vartaos.vartaosbackend.entity.Folder;
import com.vartaos.vartaosbackend.entity.Note;
import com.vartaos.vartaosbackend.repository.FolderRepository;
import com.vartaos.vartaosbackend.repository.NoteRepository;
import org.springframework.stereotype.Service;

/**
 * Service responsible for note-related business logic.
 */
@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final FolderRepository folderRepository;

    public NoteService(NoteRepository noteRepository,
                       FolderRepository folderRepository) {
        this.noteRepository = noteRepository;
        this.folderRepository = folderRepository;
    }

    /**
     * Creates a new note inside a folder.
     *
     * @param request Request containing note details.
     * @return Created note.
     */
    public NoteResponse createNote(CreateNoteRequest request) {

        // Find the folder where the note will be created
        Folder folder = folderRepository.findById(request.getFolderId())
                .orElseThrow(() -> new RuntimeException("Folder not found."));

        // Create the note
        Note note = Note.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .type(request.getType())
                .folder(folder)
                .build();

        // Save note
        noteRepository.save(note);

        // Return response
        return NoteResponse.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .type(note.getType())
                .folderId(folder.getId())
                .build();
    }

    /**
     * Retrieves a note by its ID.
     *
     * @param id ID of the note.
     * @return Note information.
     */
    public NoteResponse getNote(Long id) {

        // Find the note by ID
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found."));

        // Convert entity to response DTO
        return NoteResponse.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .type(note.getType())
                .folderId(note.getFolder().getId())
                .build();
    }

    /**
     * Updates an existing note.
     *
     * @param id      ID of the note to update.
     * @param request Request containing the updated note details.
     * @return Updated note.
     */
    public NoteResponse updateNote(Long id,
                                   UpdateNoteRequest request) {

        // Find the note
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found."));

        // Update note details
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        note.setType(request.getType());

        // Save updated note
        noteRepository.save(note);

        // Return updated note
        return NoteResponse.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .type(note.getType())
                .folderId(note.getFolder().getId())
                .build();
    }

    /**
     * Deletes a note by its ID.
     *
     * @param id ID of the note to delete.
     */
    public void deleteNote(Long id) {

        // Find the note
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found."));

        // Delete the note
        noteRepository.delete(note);
    }

    /**
     * Moves a note to another folder.
     *
     * @param id      ID of the note to move.
     * @param request Request containing the destination folder ID.
     * @return Updated note information.
     */
    public NoteResponse moveNote(Long id,
                                 MoveNoteRequest request) {

        // Find the note
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found."));

        // Find destination folder
        Folder folder = folderRepository.findById(request.getFolderId())
                .orElseThrow(() -> new RuntimeException("Folder not found."));

        // Move note
        note.setFolder(folder);

        // Save updated note
        noteRepository.save(note);

        // Return updated response
        return NoteResponse.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .type(note.getType())
                .folderId(folder.getId())
                .build();
    }
}