package com.vartaos.vartaosbackend.controller;

import com.vartaos.vartaosbackend.dto.search.SearchResponse;
import com.vartaos.vartaosbackend.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller responsible for
 * workspace-wide search operations.
 *
 * Provides APIs for searching folders
 * and notes using a single keyword.
 */
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    /**
     * Service responsible for search operations.
     */
    private final SearchService searchService;

    /**
     * Performs a global search across the workspace.
     *
     * Searches:
     * - Folder names
     * - Note titles
     * - Note content
     *
     * Example:
     *
     * GET /api/search?query=java
     *
     * @param keyword Search keyword.
     * @return Combined search results.
     */
    @GetMapping
    public SearchResponse search(
            @RequestParam("query") String keyword) {

        return searchService.search(keyword);
    }

}