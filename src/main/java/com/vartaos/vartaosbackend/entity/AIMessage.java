package com.vartaos.vartaosbackend.entity;

import com.vartaos.vartaosbackend.entity.enums.MessageSender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a single message inside an AI conversation.
 *
 * @author Ritwik
 * @since VartaOS v1
 */
@Entity
@Table(name = "ai_messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIMessage {

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Conversation to which this message belongs.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private AIConversation conversation;

    /**
     * Sender of the message.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageSender sender;

    /**
     * Message content.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    /**
     * Time when the message was sent.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }
}