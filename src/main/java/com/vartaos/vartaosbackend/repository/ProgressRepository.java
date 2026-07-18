package com.vartaos.vartaosbackend.repository;

import com.vartaos.vartaosbackend.entity.Progress;
import com.vartaos.vartaosbackend.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProgressRepository extends JpaRepository<Progress, Long> {

    /**
     * Retrieves the progress tracker for a workspace.
     */
    Optional<Progress> findByWorkspace(Workspace workspace);

}