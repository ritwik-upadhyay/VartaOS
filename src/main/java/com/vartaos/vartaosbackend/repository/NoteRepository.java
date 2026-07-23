package com.vartaos.vartaosbackend.repository;

import com.vartaos.vartaosbackend.entity.Folder;
import com.vartaos.vartaosbackend.entity.Note;
import com.vartaos.vartaosbackend.entity.enums.NoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import com.vartaos.vartaosbackend.entity.Workspace;

import java.util.List;
import java.util.Optional;

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

    Optional<Note> findByFolderAndType(Folder folder,
                                       NoteType type);

    Optional<Note> findByFolderAndTitleIgnoreCase(Folder folder,
                                                  String title);

    /**
     * Finds notes in a workspace whose titles contain the given keyword,
     * ignoring letter case.
     *
     * @param workspace Workspace to search within.
     * @param keyword   Search keyword.
     * @return Matching notes.
     */
    List<Note> findByFolderWorkspaceAndTitleContainingIgnoreCase(
            Workspace workspace,
            String keyword
    );

    /**
     * Finds notes in a workspace whose content contains the given keyword,
     * ignoring letter case.
     *
     * @param workspace Workspace to search within.
     * @param keyword   Search keyword.
     * @return Matching notes.
     */
    List<Note> findByFolderWorkspaceAndContentContainingIgnoreCase(
            Workspace workspace,
            String keyword
    );

    /**
     * Finds notes in a workspace whose title exactly matches the given keyword,
     * ignoring letter case.
     *
     * @param workspace Workspace to search within.
     * @param keyword   Search keyword.
     * @return Matching notes.
     */
    List<Note> findByFolderWorkspaceAndTitleIgnoreCase(
            Workspace workspace,
            String keyword
    );

    List<Note> findByFolderWorkspaceAndTagsNameContainingIgnoreCase(
            Workspace workspace,
            String keyword
    );
}
