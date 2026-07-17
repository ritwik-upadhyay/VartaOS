package com.vartaos.vartaosbackend.repository;

import com.vartaos.vartaosbackend.entity.Tag;
import com.vartaos.vartaosbackend.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for performing database
 * operations on tags.
 */
public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * Finds a tag by its name within
     * a specific workspace.
     *
     * Used to prevent duplicate tag names
     * inside the same workspace.
     *
     * @param name Tag name.
     * @param workspace Workspace that owns the tag.
     * @return Matching tag, if found.
     */
    Optional<Tag> findByNameAndWorkspace(String name, Workspace workspace);

    /**
     * Returns all tags belonging to
     * the specified workspace.
     *
     * @param workspace Workspace.
     * @return List of tags.
     */
    List<Tag> findByWorkspace(Workspace workspace);

}