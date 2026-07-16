package com.vartaos.vartaosbackend.controller;

import com.vartaos.vartaosbackend.dto.note.CreateNoteRequest;
import com.vartaos.vartaosbackend.dto.note.NoteResponse;
import com.vartaos.vartaosbackend.dto.note.UpdateNoteRequest;
import com.vartaos.vartaosbackend.dto.note.MoveNoteRequest;
import com.vartaos.vartaosbackend.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller responsible for note-related APIs.
 */
@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    /**
     * Constructor injection for NoteService.
     */
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    /**
     * Creates a new note.
     *
     * @param request Request containing note details.
     * @return Created note.
     */
    @PostMapping
    public NoteResponse createNote(
            @Valid @RequestBody CreateNoteRequest request) {

        return noteService.createNote(request);
    }

    /**
     * Retrieves a note by its ID.
     *
     * @param id Note ID.
     * @return Note details.
     */
    @GetMapping("/{id}")
    public NoteResponse getNote(@PathVariable Long id) {

        return noteService.getNote(id);
    }

    /**
     * Updates an existing note.
     *
     * @param id      Note ID.
     * @param request Updated note details.
     * @return Updated note.
     */
    @PutMapping("/{id}")
    public NoteResponse updateNote(@PathVariable Long id,
                                   @Valid @RequestBody UpdateNoteRequest request) {

        return noteService.updateNote(id, request);
    }

    /**
     * Deletes a note by its ID.
     *
     * @param id Note ID.
     */
    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable Long id) {

        noteService.deleteNote(id);
    }

    /**
     * Moves a note to another folder.
     *
     * @param id      ID of the note to move.
     * @param request Request containing the destination folder ID.
     * @return Updated note information.
     */
    @PutMapping("/{id}/move")
    public NoteResponse moveNote(@PathVariable Long id,
                                 @RequestBody MoveNoteRequest request) {

        return noteService.moveNote(id, request);
    }
}