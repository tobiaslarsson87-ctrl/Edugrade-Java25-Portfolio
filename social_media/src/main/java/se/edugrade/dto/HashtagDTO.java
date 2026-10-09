package se.edugrade.dto;

public record HashtagDTO(
        Long id,
        String tag,
        int weeklyGrowth

) {
    public HashtagDTO{
        if(id == null)
            throw new IllegalArgumentException("id can not be 'null'");
        if(tag== null || tag.isBlank())
            throw new IllegalArgumentException("Tag can not be 'null' or blank");
        if (weeklyGrowth < 0)
            throw new IllegalArgumentException("WeeklyGrowth can not be negative");
        // La den för att antar att den måste vara positiv för att den måste vara större?
    }
}
