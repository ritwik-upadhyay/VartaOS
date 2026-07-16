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

    /**
     * Finds notes whose titles contain the given keyword,
     * ignoring letter case.
     *
     * @param keyword Search keyword.
     * @return Matching notes.
     */
    List<Note> findByTitleContainingIgnoreCase(String keyword);

    /**
     * Finds notes whose content contains the given keyword,
     * ignoring letter case.
     *
     * @param keyword Search keyword.
     * @return Matching notes.
     */
    List<Note> findByContentContainingIgnoreCase(String keyword);

    /**
     * Finds notes whose title exactly matches the given keyword,
     * ignoring letter case.
     *
     * @param keyword Search keyword.
     * @return Matching notes.
     */
    List<Note> findByTitleIgnoreCase(String keyword);
}