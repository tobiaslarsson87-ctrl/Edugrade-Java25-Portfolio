package se.edugrade.ImportDTO;

import java.time.LocalDateTime;
import java.util.List;
// skapade en Dto för import så den importerar rätt med users.json från BackUpDatabase
public record ImportUserDTO(
        Long id,
        String username,
        String bio,
        LocalDateTime createdAt,
        List<Long> followerIds,
        List<Long> followingIds
) {
}
