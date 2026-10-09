package se.edugrade.dto;

import java.time.LocalDateTime;

/**
 *DTO för comment
 * Användsf ör att skicka data mellan lager utan att exponera entitys.
 */
public record CommentDTO(      Long id,
                               Long postId,
                               Long authorId,
                               String text,
                               LocalDateTime createdAt)

    {
        public CommentDTO {
            if (postId == null) throw new IllegalArgumentException("postId cannot be null");
            if (authorId == null) throw new IllegalArgumentException("authorId cannot be null");
            if (text == null || text.isBlank()) throw new IllegalArgumentException("text cannot be null");
            if (createdAt == null) throw new IllegalArgumentException("createdAt cannot be null ");
        }
    }


