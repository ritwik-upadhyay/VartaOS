package com.vartaos.vartaosbackend.repository;

import com.vartaos.vartaosbackend.entity.AIConversation;
import com.vartaos.vartaosbackend.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for AI conversations.
 *
 * @author Ritwik
 * @since VartaOS v1
 */
public interface AIConversationRepository extends JpaRepository<AIConversation, Long> {

    /**
     * Returns all conversations of a workspace,
     * ordered by latest activity.
     */
    List<AIConversation> findByWorkspaceOrderByUpdatedAtDesc(
            Workspace workspace
    );

}
