package com.vartaos.vartaosbackend.repository;

import com.vartaos.vartaosbackend.entity.AIConversation;
import com.vartaos.vartaosbackend.entity.AIMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for AI messages.
 *
 * @author Ritwik
 * @since VartaOS v1
 */
public interface AIMessageRepository extends JpaRepository<AIMessage, Long> {

    /**
     * Returns all messages of a conversation.
     */
    List<AIMessage> findByConversationOrderByCreatedAtAsc(
            AIConversation conversation
    );

}