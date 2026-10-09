package se.edugrade.ImportDTO;

import java.time.LocalDateTime;
import java.util.List;

// skapade en Dto för import så den importerar rätt med posts.json från BackUpDatabase
public record ImportPostDTO(
        Long id,
        Long authorId,
        String type,
        String content,
        LocalDateTime createdAt,
        List<Long>hashtagIds,
        List<Long> commentIds,
        List<Long> likeIds
)
{
}
