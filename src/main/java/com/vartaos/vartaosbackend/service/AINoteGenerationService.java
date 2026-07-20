package com.vartaos.vartaosbackend.service;

public interface AINoteGenerationService {

    void generateNotes(Long folderId);

    void markFolderCompleted(Long folderId);

}