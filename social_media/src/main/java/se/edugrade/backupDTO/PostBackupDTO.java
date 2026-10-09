package se.edugrade.backupDTO;

import se.edugrade.entities.Post;

import java.time.LocalDateTime;
import java.util.List;
// Skapade egna backupDTO för att unvika cirkulära relationer som blev en oändlig loop.
// Valde att inte nullchecka då jag vill kunna ta en backup även om vissa delar är tom.

public record PostBackupDTO(
        Long id,
        Long authorId,
        String type,
        String content,
        LocalDateTime createdAt,
        List<Long> hashtagIds,
        List<Long> commentIds,
        List<Long> likeIds
) {
    public static PostBackupDTO fromEntity(Post p) {
        return new PostBackupDTO(
                p.getId(),
                p.getAuthor().getId(),
                p.getClass().getSimpleName(),   // "TextPost", "ImagePost", etc.
                p.getContent(),
                p.getCreatedAt(),
                p.getHashtags().stream().map(h -> h.getId()).toList(),
                p.getComments().stream().map(c -> c.getId()).toList(),
                p.getLikes().stream().map(l -> l.getId()).toList()
        );
    }
}
