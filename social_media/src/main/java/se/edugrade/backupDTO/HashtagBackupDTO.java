package se.edugrade.backupDTO;

import se.edugrade.entities.Hashtag;

import java.util.List;

// Skapade egna backupDTO för att unvika cirkulära relationer som blev en oändlig loop.
// Valde att inte nullchecka då jag vill kunna ta en backup även om vissa delar är tom.

public record HashtagBackupDTO(
        Long id,
        String tag,
        List<Long> postIds
) {
    public static HashtagBackupDTO fromEntity(Hashtag h) {
        return new HashtagBackupDTO(
                h.getId(),
                h.getTag(),
                h.getPost().stream().map(p -> p.getId()).toList()
        );
    }
}
