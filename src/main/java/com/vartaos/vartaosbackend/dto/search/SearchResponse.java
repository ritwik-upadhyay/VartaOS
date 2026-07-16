package com.vartaos.vartaosbackend.dto.search;

import com.vartaos.vartaosbackend.dto.folder.FolderResponse;
import com.vartaos.vartaosbackend.dto.note.NoteResponse;
import lombok.*;

import java.util.List;

/**
 * Response DTO returned by global search.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {

    /**
     * Matching folders.
     */
    private List<FolderResponse> matchingFolders;

    /**
     * Matching notes.
     */
    private List<NoteResponse> matchingNotes;

}