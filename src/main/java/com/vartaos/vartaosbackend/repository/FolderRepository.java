package com.vartaos.vartaosbackend.repository;

import com.vartaos.vartaosbackend.entity.Folder;
import com.vartaos.vartaosbackend.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository responsible for performing database
 * operations related to folders.
 */
public interface FolderRepository extends JpaRepository<Folder, Long> {

    /**
     * Returns all root folders belonging to a workspace.
     */
    List<Folder> findByWorkspaceAndParentFolderIsNull(Workspace workspace);

    /**
     * Returns all child folders of the given parent folder.
     */
    List<Folder> findByParentFolder(Folder parentFolder);

    /**
     * Finds folders whose names contain the given keyword,
     * ignoring letter case.
     *
     * @param keyword Search keyword.
     * @return Matching folders.
     */
    List<Folder> findByWorkspaceAndNameContainingIgnoreCase(
            Workspace workspace,
            String keyword
    );
}