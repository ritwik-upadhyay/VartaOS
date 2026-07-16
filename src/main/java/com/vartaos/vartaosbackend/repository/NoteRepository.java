package com.vartaos.vartaosbackend.repository;

import com.vartaos.vartaosbackend.entity.Folder;
import com.vartaos.vartaosbackend.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository responsible for database operations
 * related to notes.
 */
public interface NoteRepository extends JpaRepository<Note, Long> {

    /**
     * Returns all notes belonging to a folder.
     *
     * @param folder Folder whose notes should be retrieved.
     * @return List of notes.
     */
    List<Note> findByFolder(Folder folder);
}