package se.edugrade.backupDTO;

import se.edugrade.entities.Users;

import java.time.LocalDateTime;
import java.util.List;
// Skapade egna backupDTO för att unvika cirkulära relationer som blev en oändlig loop.
// Valde att inte nullchecka då jag vill kunna ta en backup även om vissa delar är tom.
public record UsersBackupDTO(
        Long id,
        String username,
        String bio,
        LocalDateTime createdAt,
        List<Long> followerIds,
        List<Long> followingIds
) {
    public static UsersBackupDTO fromEntity(Users u) {
        return new UsersBackupDTO(
                u.getId(),
                u.getUserName(),
                u.getBio(),
                u.getCreatedAt(),
                u.getFollowers().stream().map(Users::getId).toList(),
                u.getFollowing().stream().map(Users::getId).toList()
        );
    }
}
