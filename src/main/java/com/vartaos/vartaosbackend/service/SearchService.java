package com.vartaos.vartaosbackend.service;

import com.vartaos.vartaosbackend.dto.folder.FolderResponse;
import com.vartaos.vartaosbackend.dto.search.SearchResponse;
import com.vartaos.vartaosbackend.entity.Folder;
import com.vartaos.vartaosbackend.entity.Note;
import com.vartaos.vartaosbackend.repository.FolderRepository;
import com.vartaos.vartaosbackend.repository.NoteRepository;
import com.vartaos.vartaosbackend.dto.note.NoteResponse;
import com.vartaos.vartaosbackend.repository.UserRepository;
import com.vartaos.vartaosbackend.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.vartaos.vartaosbackend.entity.User;
import com.vartaos.vartaosbackend.entity.Workspace;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * Service responsible for performing
 * workspace-wide searches.
 *
 * Searches folders and notes, then
 * combines the results into a single
 * response for the client.
 */
@Service
@RequiredArgsConstructor
public class SearchService {

    /**
     * Repository for folder operations.
     */
    private final FolderRepository folderRepository;

    /**
     * Repository for note operations.
     */
    private final NoteRepository noteRepository;

    /**
     * Repository for workspace operations.
     */
    private final WorkspaceRepository workspaceRepository;

    /**
     * Repository for user operations.
     */
    private final UserRepository userRepository;

    /**
     * Performs a global search across the workspace.
     *
     * Searches:
     * - Folder names
     * - Note titles
     * - Note content
     *
     * @param keyword Search keyword entered by the user.
     * @return Combined search results.
     */
    public SearchResponse search(String keyword) {

        // Search matching folders
        List<FolderResponse> matchingFolders = searchFolders(keyword);

        // Search matching notes by exact title
        List<NoteResponse> exactTitleMatches = searchExactTitle(keyword);

        // Search matching notes by title
        List<NoteResponse> titleMatches = searchNotesByTitle(keyword);

        // Search matching notes by content
        List<NoteResponse> contentMatches = searchNotesByContent(keyword);

        // Search matching notes by tag
        List<NoteResponse> tagMatches = searchNotesByTag(keyword);

        // Combine note search results
        List<NoteResponse> matchingNotes = new ArrayList<>();

        matchingNotes.addAll(exactTitleMatches);
        matchingNotes.addAll(titleMatches);
        matchingNotes.addAll(contentMatches);
        matchingNotes.addAll(tagMatches);

        // Remove duplicate notes
        matchingNotes = removeDuplicateNotes(matchingNotes);

        // Build response
        return SearchResponse.builder()
                .matchingFolders(matchingFolders)
                .matchingNotes(matchingNotes)
                .build();
    }

    /**
     * Searches for folders whose names contain
     * the specified keyword.
     *
     * @param keyword Search keyword.
     * @return List of matching folders.
     */
    private List<FolderResponse> searchFolders(String keyword) {

        Workspace workspace = getCurrentWorkspace();

        return folderRepository.findByWorkspaceAndNameContainingIgnoreCase(workspace, keyword)
                .stream()
                .map(this::mapFolder)
                .toList();
    }

    /**
     * Searches for notes whose titles contain
     * the specified keyword.
     *
     * @param keyword Search keyword.
     * @return List of matching notes.
     */
    private List<NoteResponse> searchNotesByTitle(String keyword) {

        Workspace workspace = getCurrentWorkspace();

        return noteRepository.findByFolderWorkspaceAndTitleContainingIgnoreCase(workspace, keyword)
                .stream()
                .map(this::mapNote)
                .toList();
    }

    /**
     * Searches for notes whose content contains
     * the specified keyword.
     *
     * @param keyword Search keyword.
     * @return List of matching notes.
     */
    private List<NoteResponse> searchNotesByContent(String keyword) {

        Workspace workspace = getCurrentWorkspace();

        return noteRepository.findByFolderWorkspaceAndContentContainingIgnoreCase(workspace, keyword)
                .stream()
                .map(this::mapNote)
                .toList();
    }

    /**
     * Removes duplicate notes while preserving
     * their original search order.
     *
     * @param notes List of matching notes.
     * @return List without duplicates.
     */
    private List<NoteResponse> removeDuplicateNotes(List<NoteResponse> notes) {

        Map<Long, NoteResponse> uniqueNotes = new LinkedHashMap<>();

        for (NoteResponse note : notes) {
            uniqueNotes.putIfAbsent(note.getId(), note);
        }

        return new ArrayList<>(uniqueNotes.values());
    }

    /**
     * Converts a Folder entity into a FolderResponse DTO.
     *
     * @param folder Folder entity.
     * @return Folder response.
     */
    private FolderResponse mapFolder(Folder folder) {

        return FolderResponse.builder()
                .id(folder.getId())
                .name(folder.getName())
                .parentFolderId(
                        folder.getParentFolder() != null
                                ? folder.getParentFolder().getId()
                                : null
                )
                .children(new ArrayList<>())
                .build();
    }

    /**
     * Converts a Note entity into a NoteResponse DTO.
     *
     * @param note Note entity.
     * @return Note response.
     */
    private NoteResponse mapNote(Note note) {

        return NoteResponse.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .folderId(note.getFolder().getId())
                .type(note.getType())
                .build();
    }

    /**
     * Searches for notes whose titles exactly
     * match the specified keyword.
     *
     * @param keyword Search keyword.
     * @return List of matching notes.
     */
    private List<NoteResponse> searchExactTitle(String keyword) {

        Workspace workspace = getCurrentWorkspace();

        return noteRepository.findByFolderWorkspaceAndTitleIgnoreCase(workspace, keyword)
                .stream()
                .map(this::mapNote)
                .toList();
    }

    private Workspace getCurrentWorkspace() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));

        return workspaceRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Workspace not found."));
    }

    /**
     * Searches for notes whose tags contain
     * the specified keyword.
     *
     * @param keyword Search keyword.
     * @return List of matching notes.
     */
    private List<NoteResponse> searchNotesByTag(String keyword) {

        Workspace workspace = getCurrentWorkspace();

        return noteRepository.findByFolderWorkspaceAndTagsNameContainingIgnoreCase(
                        workspace,
                        keyword
                )
                .stream()
                .map(this::mapNote)
                .toList();
    }
}