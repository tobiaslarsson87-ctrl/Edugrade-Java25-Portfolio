package se.edugrade.ImportDTO;

import java.util.List;

// skapade en Dto för import så den importerar rätt med hashtags.json från BackUpDatabase
public record ImportHashtagDTO(
        Long id,
        String tag,
        List<Long> postIds
) {
}
