package com.vartaos.vartaosbackend.repository;

import com.vartaos.vartaosbackend.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.vartaos.vartaosbackend.entity.User;


public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    Optional<Workspace> findByUser(User user);
}
